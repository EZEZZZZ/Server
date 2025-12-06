-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    picture VARCHAR(500),
    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_provider_id UNIQUE (provider, provider_id)
);

-- Couples Table
CREATE TABLE IF NOT EXISTS couples (
    id BIGSERIAL PRIMARY KEY,
    user1_id BIGINT NOT NULL,
    user2_id BIGINT NOT NULL,
    connected_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_couple_user1 FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_couple_user2 FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_user1 UNIQUE (user1_id),
    CONSTRAINT uk_user2 UNIQUE (user2_id)
);

-- Couple Codes Table
CREATE TABLE IF NOT EXISTS couple_codes (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(6) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_couple_code_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- User Emotions Table
CREATE TABLE IF NOT EXISTS user_emotions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    emotion VARCHAR(50) NOT NULL,
    intensity INTEGER NOT NULL,
    memo VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_emotion_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_intensity CHECK (intensity >= 1 AND intensity <= 5)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_provider ON users(provider, provider_id);
CREATE INDEX IF NOT EXISTS idx_couple_codes_code ON couple_codes(code);
CREATE INDEX IF NOT EXISTS idx_couple_codes_user ON couple_codes(user_id);
CREATE INDEX IF NOT EXISTS idx_couple_codes_expires ON couple_codes(expires_at);
CREATE INDEX IF NOT EXISTS idx_emotions_user_created ON user_emotions(user_id, created_at);
CREATE INDEX IF NOT EXISTS idx_emotions_user_date ON user_emotions(user_id, created_at DESC);
