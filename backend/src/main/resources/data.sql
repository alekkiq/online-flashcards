-- Example data for dev
DELETE FROM flashcards.users;
INSERT INTO flashcards.users (user_id, username, email, password, role) VALUES (1, 'aleksput', 'aleksi@aleksi.fi', '$2a$12$aaBCKsjDrT2IWffmLoLWQO/BzWz26ulrf0cJjDVqa.RPbSoyaeKm2', 'STUDENT');