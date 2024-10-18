-- aqui se ponen los scripts para la construccion de base de datos
select 1 from dual;  -- esto no es necesario pero el fichero no puede estar vacío para arrancar

INSERT INTO category(name) VALUES ('Eurogames');
INSERT INTO category(name) VALUES ('Ameritrash');
INSERT INTO category(name) VALUES ('Familiar');

INSERT INTO author(name, nationality) VALUES ('Alan R. Moon', 'US');
INSERT INTO author(name, nationality) VALUES ('Vital Lacerda', 'PT');
INSERT INTO author(name, nationality) VALUES ('Simone Luciani', 'IT');
INSERT INTO author(name, nationality) VALUES ('Perepau Llistosella', 'ES');
INSERT INTO author(name, nationality) VALUES ('Michael Kiesling', 'DE');
INSERT INTO author(name, nationality) VALUES ('Phil Walker-Harding', 'US');

INSERT INTO game(title, age, category_id, author_id) VALUES ('On Mars', '14', 1, 2);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Aventureros al tren', '8', 3, 1);
INSERT INTO game(title, age, category_id, author_id) VALUES ('1920: Wall Street', '12', 1, 4);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Barrage', '14', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Azul', '8', 3, 5);

INSERT INTO client(name) VALUES ('Juana García');
INSERT INTO client(name) VALUES ('Cristina Rodríguez');
INSERT INTO client(name) VALUES ('Rodrigo Arias');
INSERT INTO client(name) VALUES ('Margarita Perez');
INSERT INTO client(name) VALUES ('Roberto Burgos');
INSERT INTO client(name) VALUES ('Susana Oria');

INSERT INTO loan (game_id, client_id, date_start, date_end) VALUES (1, 2, '2024-10-09', '2024-10-16');
INSERT INTO loan (game_id, client_id, date_start, date_end) VALUES (2, 1, '2024-10-01', '2024-10-10');
INSERT INTO loan (game_id, client_id, date_start, date_end) VALUES (3, 3, '2024-09-20', '2024-09-25');
INSERT INTO loan (game_id, client_id, date_start, date_end) VALUES (4, 5, '2024-10-09', '2024-10-16');
INSERT INTO loan (game_id, client_id, date_start, date_end) VALUES (5, 4, '2024-07-03', '2024-07-13');
INSERT INTO loan (game_id, client_id, date_start, date_end) VALUES (6, 6, '2024-09-10', '2024-09-15');
