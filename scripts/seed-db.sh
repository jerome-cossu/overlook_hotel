#!/usr/bin/env bash
# seed-db.sh — apply db/seed-data.sql to the app database if present
# Date: 2025-09-24
set -euo pipefail

DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_USER="${DB_USER:-overlook_user}"
DB_PASS="${DB_PASS:-overlook_pass}"
DB_NAME="${DB_NAME:-overlook_hotel}"
SEED_FILE="${SEED_FILE:-db/seed-data.sql}"

print_help() {
  cat <<EOF
Usage: $0 [--help]

Applies ${SEED_FILE} to ${DB_NAME} as ${DB_USER}. Script is idempotent if seed file is written that way.
EOF
}

while [[ "${#}" -gt 0 ]]; do
  case "$1" in
    --help) print_help; exit 0 ;;
    *) echo "Unknown option: $1"; print_help; exit 2 ;;
  esac
done

if [[ ! -f "${SEED_FILE}" ]]; then
  echo "No seed file at ${SEED_FILE}; nothing to do."
  exit 0
fi

echo "Applying seed file ${SEED_FILE} to ${DB_NAME}..."
PGPASSWORD="${DB_PASS}" psql -v ON_ERROR_STOP=1 -U "${DB_USER}" -h "${DB_HOST}" -p "${DB_PORT}" -d "${DB_NAME}" -f "${SEED_FILE}"
echo "Seed applied."
