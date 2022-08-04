DROP TABLE IF EXISTS schedules;

create table schedules(
    id  SERIAL PRIMARY KEY,
	doctor varchar(50) not null,
	interval_start timestamp not null,
	interval_end timestamp not null,
	services text[] not null,
	still_available boolean not null,
	UNIQUE (doctor, interval_start, interval_end)
);

INSERT INTO schedules (doctor, interval_start, interval_end, services, still_available) VALUES
    ('doctor1', '2022-08-10 08:00', '2022-08-10 09:00', ARRAY ['DERMATOLOGIST','INFECTIOUS_DISEASE'], FALSE),
    ('doctor1', '2022-08-10 09:00', '2022-08-10 10:00', ARRAY ['DERMATOLOGIST','INFECTIOUS_DISEASE'], FALSE),
    ('doctor1', '2022-08-10 10:00', '2022-08-10 11:00', ARRAY ['DERMATOLOGIST','INFECTIOUS_DISEASE'], FALSE),
    ('doctor1', '2022-08-10 11:00', '2022-08-10 12:00', ARRAY ['DERMATOLOGIST','INFECTIOUS_DISEASE'], FALSE),
    ('doctor1', '2022-08-10 12:00', '2022-08-10 13:00', ARRAY ['CARDIOLOGIST'], TRUE),
    ('doctor2', '2022-08-10 12:00', '2022-08-10 13:00', ARRAY ['DERMATOLOGIST','INFECTIOUS_DISEASE'], TRUE),
    ('doctor2', '2022-08-10 13:00', '2022-08-10 14:00', ARRAY ['DERMATOLOGIST','INFECTIOUS_DISEASE'], TRUE);

