-- V1__Create_initial_schema.sql
-- Place this file in src/main/resources/db/migration/

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'PREMIUM_USER', 'FREE_USER')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE', 'BANNED', 'PENDING_VERIFICATION')),
    profile_image_url TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create artists table
CREATE TABLE artists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    biography VARCHAR(500),
    genre VARCHAR(20) NOT NULL CHECK (genre IN ('POP', 'ROCK', 'HIP_HOP', 'JAZZ', 'CLASSICAL', 'ELECTRONIC', 'COUNTRY', 'R_AND_B', 'REGGAE', 'BLUES', 'METAL', 'FOLK', 'INDIE', 'LATIN', 'WORLD', 'ALTERNATIVE', 'PUNK', 'FUNK', 'DISCO', 'OTHER')),
    profile_image_url TEXT,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create albums table
CREATE TABLE albums (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    release_date DATE,
    genre VARCHAR(20) NOT NULL CHECK (genre IN ('POP', 'ROCK', 'HIP_HOP', 'JAZZ', 'CLASSICAL', 'ELECTRONIC', 'COUNTRY', 'R_AND_B', 'REGGAE', 'BLUES', 'METAL', 'FOLK', 'INDIE', 'LATIN', 'WORLD', 'ALTERNATIVE', 'PUNK', 'FUNK', 'DISCO', 'OTHER')),
    artist_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_albums_artist FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE
);

-- Create songs table
CREATE TABLE songs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    duration_seconds INTEGER NOT NULL,
    file_url TEXT NOT NULL,
    genre VARCHAR(20) NOT NULL CHECK (genre IN ('POP', 'ROCK', 'HIP_HOP', 'JAZZ', 'CLASSICAL', 'ELECTRONIC', 'COUNTRY', 'R_AND_B', 'REGGAE', 'BLUES', 'METAL', 'FOLK', 'INDIE', 'LATIN', 'WORLD', 'ALTERNATIVE', 'PUNK', 'FUNK', 'DISCO', 'OTHER')),
    play_count BIGINT NOT NULL DEFAULT 0,
    artist_id BIGINT NOT NULL,
    album_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_songs_artist FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE,
    CONSTRAINT fk_songs_album FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE SET NULL
);

-- Create playlists table
CREATE TABLE playlists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    is_public BOOLEAN NOT NULL DEFAULT TRUE,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_playlists_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create junction table for user favorite songs
CREATE TABLE user_favorite_songs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_favorite_songs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_favorite_songs_song FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE,
    UNIQUE(user_id, song_id)
);

-- Create junction table for user favorite albums
CREATE TABLE user_favorite_albums (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    album_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_favorite_albums_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_favorite_albums_album FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE,
    UNIQUE(user_id, album_id)
);

-- Create junction table for user followed artists
CREATE TABLE user_followed_artists (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    artist_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_followed_artists_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_followed_artists_artist FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE,
    UNIQUE(user_id, artist_id)
);

-- Create junction table for playlist songs
CREATE TABLE playlist_songs (
    id BIGSERIAL PRIMARY KEY,
    playlist_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    position INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_playlist_songs_playlist FOREIGN KEY (playlist_id) REFERENCES playlists(id) ON DELETE CASCADE,
    CONSTRAINT fk_playlist_songs_song FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE,
    UNIQUE(playlist_id, song_id),
    UNIQUE(playlist_id, position)
);

-- Create indexes for better query performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_name ON users(name);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_created_at ON users(created_at);

CREATE INDEX idx_artists_name ON artists(name);
CREATE INDEX idx_artists_genre ON artists(genre);
CREATE INDEX idx_artists_verified ON artists(verified);
CREATE INDEX idx_artists_created_at ON artists(created_at);

CREATE INDEX idx_albums_title ON albums(title);
CREATE INDEX idx_albums_artist_id ON albums(artist_id);
CREATE INDEX idx_albums_genre ON albums(genre);
CREATE INDEX idx_albums_release_date ON albums(release_date);
CREATE INDEX idx_albums_created_at ON albums(created_at);

CREATE INDEX idx_songs_title ON songs(title);
CREATE INDEX idx_songs_artist_id ON songs(artist_id);
CREATE INDEX idx_songs_album_id ON songs(album_id);
CREATE INDEX idx_songs_genre ON songs(genre);
CREATE INDEX idx_songs_play_count ON songs(play_count);
CREATE INDEX idx_songs_created_at ON songs(created_at);

CREATE INDEX idx_playlists_user_id ON playlists(user_id);
CREATE INDEX idx_playlists_name ON playlists(name);
CREATE INDEX idx_playlists_is_public ON playlists(is_public);
CREATE INDEX idx_playlists_created_at ON playlists(created_at);

-- Indexes for junction tables to improve join performance
CREATE INDEX idx_user_favorite_songs_user_id ON user_favorite_songs(user_id);
CREATE INDEX idx_user_favorite_songs_song_id ON user_favorite_songs(song_id);

CREATE INDEX idx_user_favorite_albums_user_id ON user_favorite_albums(user_id);
CREATE INDEX idx_user_favorite_albums_album_id ON user_favorite_albums(album_id);

CREATE INDEX idx_user_followed_artists_user_id ON user_followed_artists(user_id);
CREATE INDEX idx_user_followed_artists_artist_id ON user_followed_artists(artist_id);

CREATE INDEX idx_playlist_songs_playlist_id ON playlist_songs(playlist_id);
CREATE INDEX idx_playlist_songs_song_id ON playlist_songs(song_id);
CREATE INDEX idx_playlist_songs_position ON playlist_songs(position);

-- Insert sample data for testing
INSERT INTO users (email, name, password, role, status) VALUES 
('admin@spotify.com', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFdELJvktbQfmE7p2mCpO/O', 'ADMIN', 'ACTIVE'), -- password: admin123
('john.doe@example.com', 'johndoe', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFdELJvktbQfmE7p2mCpO/O', 'PREMIUM_USER', 'ACTIVE'), -- password: admin123
('jane.smith@example.com', 'janesmith', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFdELJvktbQfmE7p2mCpO/O', 'FREE_USER', 'ACTIVE'), -- password: admin123
('bob.wilson@example.com', 'bobwilson', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFdELJvktbQfmE7p2mCpO/O', 'FREE_USER', 'ACTIVE'); -- password: admin123

INSERT INTO artists (name, biography, genre, verified) VALUES 
('The Beatles', 'Legendary British rock band formed in Liverpool in 1960', 'ROCK', TRUE),
('Taylor Swift', 'American singer-songwriter known for narrative songs about her personal life', 'POP', TRUE),
('Miles Davis', 'Iconic American jazz trumpeter, bandleader, and composer', 'JAZZ', TRUE),
('Daft Punk', 'French electronic music duo formed in 1993', 'ELECTRONIC', TRUE),
('Johnny Cash', 'American singer-songwriter and country music legend', 'COUNTRY', TRUE);

INSERT INTO albums (title, description, release_date, genre, artist_id) VALUES 
('Abbey Road', 'The Beatles eleventh studio album and final recorded album', '1969-09-26', 'ROCK', 1),
('Sgt. Peppers Lonely Hearts Club Band', 'Eighth studio album by The Beatles', '1967-06-01', 'ROCK', 1),
('1989', 'Taylor Swift fifth studio album, her first official pop album', '2014-10-27', 'POP', 2),
('Folklore', 'Taylor Swift eighth studio album, indie folk and alternative rock', '2020-07-24', 'INDIE', 2),
('Kind of Blue', 'Miles Davis studio album, regarded as one of the greatest jazz recordings', '1959-08-17', 'JAZZ', 3),
('Random Access Memories', 'Daft Punk fourth studio album', '2013-05-17', 'ELECTRONIC', 4),
('At Folsom Prison', 'Johnny Cash live album recorded at Folsom State Prison', '1968-05-01', 'COUNTRY', 5);

INSERT INTO songs (title, duration_seconds, file_url, genre, artist_id, album_id, play_count) VALUES 
-- The Beatles songs
('Come Together', 259, 'https://example.com/songs/come-together.mp3', 'ROCK', 1, 1, 15420000),
('Something', 183, 'https://example.com/songs/something.mp3', 'ROCK', 1, 1, 12350000),
('Here Comes the Sun', 185, 'https://example.com/songs/here-comes-the-sun.mp3', 'ROCK', 1, 1, 18720000),
('With a Little Help from My Friends', 164, 'https://example.com/songs/with-a-little-help.mp3', 'ROCK', 1, 2, 8940000),
('Lucy in the Sky with Diamonds', 208, 'https://example.com/songs/lucy-in-the-sky.mp3', 'ROCK', 1, 2, 7630000),

-- Taylor Swift songs
('Shake It Off', 219, 'https://example.com/songs/shake-it-off.mp3', 'POP', 2, 3, 42500000),
('Blank Space', 231, 'https://example.com/songs/blank-space.mp3', 'POP', 2, 3, 38900000),
('Cardigan', 239, 'https://example.com/songs/cardigan.mp3', 'INDIE', 2, 4, 25100000),
('The 1', 210, 'https://example.com/songs/the-1.mp3', 'INDIE', 2, 4, 18200000),

-- Miles Davis songs
('So What', 563, 'https://example.com/songs/so-what.mp3', 'JAZZ', 3, 5, 3200000),
('All Blues', 686, 'https://example.com/songs/all-blues.mp3', 'JAZZ', 3, 5, 2800000),

-- Daft Punk songs
('Get Lucky', 367, 'https://example.com/songs/get-lucky.mp3', 'ELECTRONIC', 4, 6, 28400000),
('Instant Crush', 337, 'https://example.com/songs/instant-crush.mp3', 'ELECTRONIC', 4, 6, 15600000),

-- Johnny Cash songs
('Folsom Prison Blues', 175, 'https://example.com/songs/folsom-prison-blues.mp3', 'COUNTRY', 5, 7, 9200000),
('Jackson', 174, 'https://example.com/songs/jackson.mp3', 'COUNTRY', 5, 7, 6800000);

-- Create some playlists for sample users
INSERT INTO playlists (name, description, is_public, user_id) VALUES 
('My Favorites', 'All time favorite songs', TRUE, 2),
('Rock Classics', 'Best rock songs of all time', TRUE, 2),
('Chill Vibes', 'Relaxing music for study', TRUE, 3),
('Workout Mix', 'High energy songs for gym', FALSE, 3);

-- Add songs to playlists
INSERT INTO playlist_songs (playlist_id, song_id, position) VALUES 
-- John's favorites playlist
(1, 1, 1), -- Come Together
(1, 6, 2), -- Shake It Off
(1, 13, 3), -- Get Lucky
(1, 3, 4), -- Here Comes the Sun

-- John's rock classics playlist
(2, 1, 1), -- Come Together
(2, 2, 2), -- Something
(2, 3, 3), -- Here Comes the Sun
(2, 4, 4), -- With a Little Help from My Friends

-- Jane's chill vibes playlist
(3, 8, 1), -- Cardigan
(3, 9, 2), -- The 1
(3, 10, 3), -- So What
(3, 11, 4), -- All Blues

-- Jane's workout mix playlist
(4, 6, 1), -- Shake It Off
(4, 13, 2), -- Get Lucky
(4, 1, 3); -- Come Together

-- Add some favorite songs for users
INSERT INTO user_favorite_songs (user_id, song_id) VALUES 
(2, 1), -- John likes Come Together
(2, 3), -- John likes Here Comes the Sun
(2, 6), -- John likes Shake It Off
(3, 8), -- Jane likes Cardigan
(3, 10), -- Jane likes So What
(4, 13); -- Bob likes Get Lucky

-- Add some favorite albums for users
INSERT INTO user_favorite_albums (user_id, album_id) VALUES 
(2, 1), -- John likes Abbey Road
(2, 3), -- John likes 1989
(3, 4), -- Jane likes Folklore
(3, 5); -- Jane likes Kind of Blue

-- Add some followed artists for users
INSERT INTO user_followed_artists (user_id, artist_id) VALUES 
(2, 1), -- John follows The Beatles
(2, 2), -- John follows Taylor Swift
(3, 2), -- Jane follows Taylor Swift
(3, 3), -- Jane follows Miles Davis
(4, 4); -- Bob follows Daft Punk