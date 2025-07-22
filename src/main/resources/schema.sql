CREATE TABLE IF NOT EXISTS members (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  address VARCHAR(255),
  email VARCHAR(100) UNIQUE,
  phone VARCHAR(30),
  start_date DATE,
  duration INT,
  membership_type VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS tournaments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  location VARCHAR(120),
  entry_fee DECIMAL(10,2),
  prize_amount DECIMAL(12,2)
);

CREATE TABLE IF NOT EXISTS member_tournament (
  member_id BIGINT NOT NULL,
  tournament_id BIGINT NOT NULL,
  PRIMARY KEY (member_id, tournament_id),
  FOREIGN KEY (member_id) REFERENCES members(id),
  FOREIGN KEY (tournament_id) REFERENCES tournaments(id)
);
