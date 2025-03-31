BEGIN;

CREATE TABLE dim_sport_type (
    id BIGINT PRIMARY KEY,
    type_name VARCHAR(100));

CREATE TABLE dim_sport (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    sport_type_id BIGINT REFERENCES dim_sport_type(id),
    events_count BIGINT,
    avg_start_diff interval);

CREATE TABLE dim_event (
    id BIGINT PRIMARY KEY,
    sport_id BIGINT REFERENCES dim_sport(id),
    event_name VARCHAR(200),
    actual_start_diff interval);

CREATE TABLE dim_selection (
    id BIGINT PRIMARY KEY,
    selection_name VARCHAR(40),
    event_id BIGINT REFERENCES dim_event(id),
    plays_count BIGINT,
    median_odds FLOAT);   	

CREATE TABLE dim_agg_selection_plays_count_per_year (
    selection_id BIGINT REFERENCES dim_selection(id),
    year INT,
    total_plays_count BIGINT,
    PRIMARY KEY (selection_id, year)); 

CREATE TABLE dim_date (
    id BIGINT PRIMARY KEY,
    full_date DATE,
    year INT);   

CREATE TABLE fact_bets (
    id BIGINT PRIMARY KEY, 
    event_id BIGINT REFERENCES dim_event(id),
    selection_id BIGINT REFERENCES dim_selection(id),
    sport_id BIGINT REFERENCES dim_sport(id),
    date_id BIGINT REFERENCES dim_date(id),
    odds FLOAT);

ALTER TABLE dim_sport ALTER COLUMN avg_start_diff TYPE interval USING (avg_start_diff * INTERVAL '1 second');

COMMIT;