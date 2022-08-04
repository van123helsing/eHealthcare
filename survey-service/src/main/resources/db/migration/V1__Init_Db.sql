DROP TABLE IF EXISTS surveys;

create table surveys(
    id  SERIAL PRIMARY KEY,
	appointment_id varchar(50) not null,
	rating integer CHECK (rating > 0 AND rating <= 5),
	UNIQUE (appointment_id)
);

INSERT INTO surveys (appointment_id, rating) VALUES
    (1, 4),
    (2, 3),
    (3, 1),
    (4, 5);

