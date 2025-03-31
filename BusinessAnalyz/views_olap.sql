CREATE OR REPLACE VIEW v_top_sports_by_events AS
WITH ranked_sports AS (
    SELECT 
        dst.type_name AS sport_type,
        ds.name AS sport_name,
        ds.events_count,
        DENSE_RANK() OVER (ORDER BY ds.events_count DESC) AS overall_rank
    FROM 
        dim_sport ds
    JOIN 
        dim_sport_type dst ON ds.sport_type_id = dst.id
    WHERE 
        ds.events_count > 0
)
SELECT 
    sport_type,
    sport_name,
    events_count,
    overall_rank AS rank
FROM 
    ranked_sports
WHERE 
    overall_rank <= 10
ORDER BY 
    events_count DESC, 
    sport_type, 
    sport_name;

CREATE OR REPLACE VIEW v_soccer_bets_above_median_odds AS
SELECT 
    fb.id AS bet_id,
    ds.median_odds AS selection_median_odds,
    fb.odds AS bet_odds,
    fb.odds - ds.median_odds AS odds_difference
FROM 
    fact_bets fb
JOIN dim_selection ds ON fb.selection_id = ds.id
JOIN dim_date dd ON fb.date_id = dd.id
JOIN dim_event de ON fb.event_id = de.id
JOIN dim_sport dsp ON de.sport_id = dsp.id
WHERE 
    dsp.name = 'Soccer'
    AND dd.year = 2014
    AND fb.odds > ds.median_odds
ORDER BY 
    odds_difference DESC;

CREATE OR REPLACE VIEW v_top_soccer_selections_2014 AS
WITH soccer_most_played_selections AS (
    SELECT 
        dsel.id as selection_id,
        dsel.selection_name,
        daspcpy.total_plays_count AS bets_count,
        RANK() OVER (ORDER BY daspcpy.total_plays_count DESC) AS popularity_rank
    FROM 
        dim_agg_selection_plays_count_per_year daspcpy
    JOIN dim_selection dsel ON daspcpy.selection_id = dsel.id
    JOIN dim_event de ON dsel.event_id = de.id
    JOIN dim_sport ds ON de.sport_id = ds.id
    WHERE 
        ds.name = 'Soccer'
        AND daspcpy.year = 2014
)
SELECT 
    selection_id,
    selection_name,
    bets_count
FROM 
    soccer_most_played_selections
WHERE 
    popularity_rank <= 10
ORDER BY 
    bets_count DESC;

CREATE OR REPLACE VIEW v_tennis_matches_late_start AS
SELECT 
    de.id as match_id,
    de.event_name AS match_name,
    de.actual_start_diff AS actual_deviation,
    EXTRACT(EPOCH FROM de.actual_start_diff) AS deviation_seconds,
    EXTRACT(EPOCH FROM ds.avg_start_diff) AS avg_deviation_seconds,
    EXTRACT(EPOCH FROM (de.actual_start_diff - ds.avg_start_diff)) AS above_avg_seconds
FROM 
    dim_event de
JOIN 
    dim_sport ds ON de.sport_id = ds.id
WHERE 
    ds.name = 'Tennis'
    AND de.actual_start_diff IS NOT NULL
    AND de.actual_start_diff > ds.avg_start_diff
ORDER BY 
    above_avg_seconds DESC;