DROP TABLE IF EXISTS appointments;

create table appointments(
    id  SERIAL PRIMARY KEY,
	patient varchar(50) not null,
	schedule_id integer not null,
	service varchar(50) not null,
	UNIQUE (schedule_id)
);

INSERT INTO appointments (patient, schedule_id, service) VALUES
    ('patient1', 1, 'DERMATOLOGIST'),
    ('patient1', 2, 'DERMATOLOGIST'),
    ('patient2', 3, 'DERMATOLOGIST'),
    ('patient2', 4, 'DERMATOLOGIST');

