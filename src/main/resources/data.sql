-- USERS
INSERT INTO users (id, username, email, password, role) VALUES
(1, 'ashketchum', 'ash@pokemon.com', 'pikachu123', 'USER'),
(2, 'misty', 'misty@cerulean.com', 'togepi456', 'USER');

-- DESTINATIONS
INSERT INTO destinations (id, name, country, description, user_id) VALUES
(1, 'Pallet Town', 'Kanto', 'Home of Ash', 1),
(2, 'Cerulean City', 'Kanto', 'Water-type gym', 2);
