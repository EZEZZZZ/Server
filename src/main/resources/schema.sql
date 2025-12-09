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

-- Anniversaries Table
CREATE TABLE IF NOT EXISTS anniversaries (
    id BIGSERIAL PRIMARY KEY,
    couple_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    repeat BOOLEAN NOT NULL DEFAULT false,
    memo VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_anniversary_couple FOREIGN KEY (couple_id) REFERENCES couples(id) ON DELETE CASCADE
);

-- Questions Table
CREATE TABLE IF NOT EXISTS questions (
    id BIGSERIAL PRIMARY KEY,
    question VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- User Daily Questions Table
CREATE TABLE IF NOT EXISTS user_daily_questions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    date DATE NOT NULL,
    answered BOOLEAN NOT NULL DEFAULT false,
    answer VARCHAR(1000),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_daily_question_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_daily_question_question FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_question_date UNIQUE (user_id, date)
);

-- Search Histories Table
CREATE TABLE IF NOT EXISTS search_histories (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    keyword VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_search_history_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Missions Table
CREATE TABLE IF NOT EXISTS missions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT chk_mission_type CHECK (type IN ('ACTION', 'QUESTION'))
);

-- User Missions Table
CREATE TABLE IF NOT EXISTS user_missions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    mission_id BIGINT NOT NULL,
    date DATE NOT NULL,
    performed BOOLEAN NOT NULL DEFAULT false,
    performed_at TIMESTAMP,
    evidence VARCHAR(500),
    image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_mission_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_mission_mission FOREIGN KEY (mission_id) REFERENCES missions(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_mission_date UNIQUE (user_id, date)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_provider ON users(provider, provider_id);
CREATE INDEX IF NOT EXISTS idx_couple_codes_code ON couple_codes(code);
CREATE INDEX IF NOT EXISTS idx_couple_codes_user ON couple_codes(user_id);
CREATE INDEX IF NOT EXISTS idx_couple_codes_expires ON couple_codes(expires_at);
CREATE INDEX IF NOT EXISTS idx_emotions_user_created ON user_emotions(user_id, created_at);
CREATE INDEX IF NOT EXISTS idx_emotions_user_date ON user_emotions(user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_anniversaries_couple_date ON anniversaries(couple_id, date);
CREATE INDEX IF NOT EXISTS idx_anniversaries_couple ON anniversaries(couple_id);
CREATE INDEX IF NOT EXISTS idx_user_daily_questions_user_date ON user_daily_questions(user_id, date);
CREATE INDEX IF NOT EXISTS idx_user_daily_questions_date ON user_daily_questions(date);
CREATE INDEX IF NOT EXISTS idx_search_histories_user_created ON search_histories(user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_user_missions_user_date ON user_missions(user_id, date);
CREATE INDEX IF NOT EXISTS idx_user_missions_date ON user_missions(date);
