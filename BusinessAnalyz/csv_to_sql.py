import psycopg2
import pandas as pd

conn = psycopg2.connect(
    dbname="bets_olap",
    user="postgres",
    password="root",
    host="localhost"
)

df = pd.read_csv('dataset.csv')

df.columns = df.columns.str.lower() 

print("Колонки в CSV:", df.columns.tolist())

with conn.cursor() as cur:
    for _, row in df.iterrows():
        try:
            cur.execute("""
                INSERT INTO temp_import (
                    sports_id, event_id, settled_date, full_description, 
                    scheduled_off, event, dt_actual_off, selection_id,
                    selection, odds, number_bets, volume_matched,
                    latest_taken, first_taken, win_flag, in_play
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s,
                    %s, %s, %s, %s, %s, %s, %s, %s
                )
            """, (
                row['sports_id'],
                row['event_id'],
                row['settled_date'],
                row['full_description'],
                row['scheduled_off'],
                row['event'],
                row['dt_actual_off'],
                row['selection_id'],
                row['selection'],
                row['odds'],
                row['number_bets'],
                row['volume_matched'],
                row['latest_taken'],
                row['first_taken'],
                row['win_flag'],
                row['in_play']
            ))
        except Exception as e:
            print(f"Ошибка в строке {_}: {e}")
            print("Проблемные данные:", row)
            conn.rollback()
            break
    conn.commit()

conn.close()