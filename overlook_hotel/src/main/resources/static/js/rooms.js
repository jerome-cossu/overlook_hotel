const API_BASE = '/api/rooms'; 
const container = document.getElementById('rooms-container');
const modal = document.getElementById('room-details');

async function fetchRooms() {
    try {
        const res = await fetch(API_BASE);
        const rooms = await res.json();
        displayRooms(rooms);
    } catch (err) {
        container.innerHTML = '<p>Unable to log the rooms.</p>';
        console.error(err);
    }
}

function displayRooms(rooms) {
    container.innerHTML = '';
    rooms.forEach(room => {
        const card = document.createElement('div');
        card.className = 'room-card';
        card.innerHTML = `
            <h2>Room ${room.roomNumber}</h2>
            <p>${room.guest} guest(s)</p>
            <img src="/images/${room.picture}" alt="photo" id="room-picture"/>
            <ul class="features">
                ${room.features.map(f => `<li>${f.name}</li>`).join('')}
            </ul>
            <a href="/rooms/${room.id}" class="details-btn">Details</a>

            
        `;
        container.appendChild(card);
    });
}


function showDetails(room) {
    document.getElementById('modal-room-number').textContent = `Room ${room.roomNumber}`;
    document.getElementById('modal-guest').textContent = room.guest;
    document.getElementById('modal-price').textContent = room.price;
    document.getElementById('modal-picture').setAttribute("src", "/images/" + room.picture);
    document.getElementById('modal-occupancy').textContent = room.occupancy ? 'Occupied' : 'Free';
    const featureList = document.getElementById('modal-features');
    featureList.innerHTML = room.features.map(f => `<li>${f.name}</li>`).join('');
        const bookingContainer = document.getElementById('modal-booking-form');
    if (!room.occupancy) {
        bookingContainer.innerHTML = `
            <form id="booking-form-${room.id}">
                <label>Check-in:</label>
                <input type="date" name="checkIn" required>
                <label>Check-out:</label>
                <input type="date" name="checkOut" required>
                <button type="submit">Book</button>
            </form>
        `;

        const form = document.getElementById(`booking-form-${room.id}`);
        form.addEventListener('submit', async function(e) {
            e.preventDefault();
            const checkIn = form.checkIn.value;
            const checkOut = form.checkOut.value;

            try {
                const res = await fetch(`/bookings/reserve/${room.id}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: new URLSearchParams({ checkIn, checkOut })
                });

                if (res.ok) {
                    alert('Booking confirmed !');
                    closeModal();
                    fetchRooms(); // actualiser l'affichage des chambres
                } else {
                    alert('Error while booking');
                }
            } catch (err) {
                console.error(err);
                alert('Network error ');
            }
        });

    } else {
        bookingContainer.innerHTML = '<p>Room already booked</p>';
    }
    modal.style.display = 'flex';
    
}

function closeModal() {
    modal.style.display = 'none';
}

fetchRooms();

    const user = JSON.parse(localStorage.getItem("user"));

    if (user) {
        const nav = document.querySelector("nav");
        const loginBtn = nav.querySelector(".login-btn");
        if (loginBtn) {
            loginBtn.textContent = "Hello " + user.firstName;
            loginBtn.removeAttribute("href");
        }
    }
