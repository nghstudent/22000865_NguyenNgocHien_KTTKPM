CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    movie VARCHAR(255) NOT NULL,
    show_date VARCHAR(20),
    show_time VARCHAR(20),
    seats INTEGER NOT NULL,
    seat_numbers TEXT,
    payment_method VARCHAR(50),
    status VARCHAR(50)
);
