DROP TABLE IF EXISTS users cascade;

CREATE TABLE users (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  balance DECIMAL NOT NULL
);

INSERT INTO users (name, balance) VALUES
  ('Gustav', 102),
  ('Eva', 505.4),
  ('Per', 30455.4);