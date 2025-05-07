import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from datetime import datetime

df = pd.read_csv("betfair_140901.csv", usecols=[
    "SPORTS_ID", 
    'EVENT_ID', 
    'SETTLED_DATE', 
    'SCHEDULED_OFF', 
    'EVENT', 
    'DT ACTUAL_OFF', 
    'SELECTION_ID', 
    'SELECTION', 
    'ODDS'
])

sport_ids = df['SPORTS_ID'].unique()

known_sport_names = {
    1: "Soccer",
    2: "Tennis",
    4339: "Greyhound Racing",
    4: "Cricket",
    7511: "Baseball",
    3: "Golf",
    5: "Rugby Union",
    8: "Motor Sport",
    7522: "Basketball",
    6423: "American Football",
    6231: "Financial Bets",
    998917: "Volleyball"
}

df["SPORTS_NAME"] = df["SPORTS_ID"].map(known_sport_names)

df["SPORTS_NAME"] = df["SPORTS_NAME"].fillna("Sport " + df["SPORTS_ID"].astype(str))

print(df.head)

# За 2014 год предоставить список матчей (теннис) – 
# в которых наблюдалось наибольшая задержка начала старта события по сравнению с запланированным
# Вывести список из 10 идентификаторов матчей, отсортированных по убыванию время задержки
tennis_data = df[df['SPORTS_NAME'] == 'Tennis']

unique_events_tennis_data = tennis_data[['EVENT_ID', 'SCHEDULED_OFF', 'DT ACTUAL_OFF']].drop_duplicates(subset=['EVENT_ID'])

def parse_datetime(dt_str):
    if pd.isna(dt_str):
        return None
    formats = [
        '%d-%m-%Y %H:%M',     # 04-09-2014 15:30
        '%d-%m-%Y %H:%M:%S',  # 04-09-2014 15:30:30
        '%d/%m/%Y %H:%M',     # Альтернативный формат с /
        '%d/%m/%Y %H:%M:%S'   # Альтернативный формат с / и секундами
    ]
    for fmt in formats:
        try:
            return datetime.strptime(dt_str, fmt)
        except ValueError:
            continue
    return None

unique_events_tennis_data['scheduled_off_dt'] = unique_events_tennis_data['SCHEDULED_OFF'].apply(parse_datetime)
unique_events_tennis_data['actual_off_dt'] = unique_events_tennis_data['DT ACTUAL_OFF'].apply(parse_datetime)

unique_events_tennis_data = unique_events_tennis_data.dropna(subset=['scheduled_off_dt', 'actual_off_dt'])

unique_events_tennis_data['AVERAGE_START_DELAY'] = (unique_events_tennis_data['actual_off_dt'] - unique_events_tennis_data['scheduled_off_dt']).apply(lambda x: x.total_seconds())

top_10_tennis_delayed = unique_events_tennis_data.sort_values('AVERAGE_START_DELAY', ascending=False).iloc[1:11]

top_10_tennis_delayed_events = dict(zip(top_10_tennis_delayed['EVENT_ID'], top_10_tennis_delayed['AVERAGE_START_DELAY']))

for event_id, delay in top_10_tennis_delayed_events.items():
    print(f"ID матча: {event_id}, Задержка, мин. : {delay/60:.2f}")

events_ids = [str(key) for key, _ in top_10_tennis_delayed_events.items()]
delays = [value/60 for _, value in top_10_tennis_delayed_events.items()]

plt.figure(figsize=(12, 6))

x_positions = range(len(events_ids))

plt.plot(x_positions, delays, 
         marker='o',
         markersize=10,
         linestyle='-',
         linewidth=2,
         color='#1f77b4',
         alpha=0.7)

i = 0

for event_id, delay_seconds in top_10_tennis_delayed_events.items():
    # Подпись значения
    plt.text(i, delay_seconds/60 + 0.02, f"{delay_seconds/60:.2f}",
             ha='center', 
             va='bottom',
             fontsize=10,
             bbox=dict(facecolor='white', alpha=0.8))
    
    plt.text(i, -5, str(event_id),
             ha='center',
             va='top',
             fontsize=10,
             rotation=45)
    
    i += 1

# Настройки осей
plt.xticks(x_positions, [])  # Убираем стандартные подписи
plt.xlabel("ID матча", fontsize=12)
plt.ylabel("Задержка начала, мин.", fontsize=12)
plt.title("Теннисные матчи с самой большой задержкой начала", fontsize=14)

plt.ylim(200, max(delays) * 1.1)
plt.xlim(-0.5, len(events_ids)-1)

plt.grid(True, linestyle='--', alpha=0.7)

plt.tight_layout()
plt.show()
