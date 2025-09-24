#!/usr/bin/env bash
# run-local.sh — create DB user + database for local development (idempotent)
# Assumes PostgreSQL is running and superuser access is available
# Date: 2025-09-24
set -euo pipefail

DB_SUPERUSER="${DB_SUPERUSER:-postgres}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"

DB_USER="${DB_USER:-overlook_user}"
DB_PASS="${DB_PASS:-overlook_pass}"
DB_NAME="${DB_NAME:-overlook_hotel}"

NO_PROMPT=0

print_help() {
  cat <<EOF
Usage: $0 [--no-prompt] [--help]

Creates PostgreSQL role and database for local development if they don't exist.
Environment variables override defaults: DB_USER, DB_PASS, DB_NAME, DB_HOST, DB_PORT, DB_SUPERUSER
EOF
}

while [[ "${#}" -gt 0 ]]; do
  case "$1" in
    --no-prompt) NO_PROMPT=1; shift ;;
    --help) print_help; exit 0 ;;
    *) echo "Unknown option: $1"; print_help; exit 2 ;;
  esac
done

confirm_or_exit() {
  if [[ "$NO_PROMPT" -eq 1 ]]; then return 0; fi
  read -r -p "$1 [y/N]: " resp
  case "${resp,,}" in y|yes) return 0 ;; *) echo "Aborted."; exit 1 ;; esac
}

psql_super() {
  if [[ "$(id -u)" -eq 0 ]]; then
    sudo -u "${DB_SUPERUSER}" psql -h "${DB_HOST}" -p "${DB_PORT}" -v ON_ERROR_STOP=1 "$@"
  else
    psql -U "${DB_SUPERUSER}" -h "${DB_HOST}" -p "${DB_PORT}" -v ON_ERROR_STOP=1 "$@"
  fi
}

echo "Create DB '${DB_NAME}' and role '${DB_USER}' on ${DB_HOST}:${DB_PORT}"
confirm_or_exit "Proceed?"

# create role if missing
psql_super <<SQL || true
DO
\$do\$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = '${DB_USER}') THEN
    CREATE ROLE ${DB_USER} WITH LOGIN PASSWORD '${DB_PASS}';
    RAISE NOTICE 'Created role ${DB_USER}';
  ELSE
    RAISE NOTICE 'Role ${DB_USER} already exists';
  END IF;
END
\$do\$;
SQL

# create database if missing
psql_super <<'SQL' || true
DO
$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = current_setting('app.dbname','true')) THEN
    PERFORM current_setting('app.dbname');
  END IF;
END;
$;
SQL

# The above block uses current_setting as a safe no-op on some systems.
# Fallback to explicit create if DB doesn't exist:
DB_EXISTS=$(psql -U "${DB_SUPERUSER}" -h "${DB_HOST}" -p "${DB_PORT}" -tAc "SELECT 1 FROM pg_database WHERE datname='${DB_NAME}'" || echo "")
if [[ "${DB_EXISTS}" != "1" ]]; then
  echo "Creating database ${DB_NAME}..."
  psql_super -c "CREATE DATABASE ${DB_NAME} OWNER ${DB_USER};"
else
  echo "Database ${DB_NAME} already exists"
fi

# grant privileges
psql_super -c "GRANT ALL PRIVILEGES ON DATABASE ${DB_NAME} TO ${DB_USER};" || true

echo "Done. Verify connection with:"
echo "  PGPASSWORD=${DB_PASS} psql -U ${DB_USER} -h ${DB_HOST} -p ${DB_PORT} -d ${DB_NAME} -c '\\l'"
