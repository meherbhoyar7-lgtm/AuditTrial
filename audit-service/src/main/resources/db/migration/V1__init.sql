CREATE TABLE user_account (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(80) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(30) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE activity_log (
  id BIGSERIAL PRIMARY KEY,
  actor VARCHAR(80) NOT NULL,
  action VARCHAR(120) NOT NULL,
  entity_type VARCHAR(80),
  entity_id VARCHAR(80),
  details TEXT,
  ip_address VARCHAR(80),
  user_agent TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_activity_created_at ON activity_log(created_at DESC);
CREATE INDEX idx_activity_actor ON activity_log(actor);
CREATE INDEX idx_activity_action ON activity_log(action);
