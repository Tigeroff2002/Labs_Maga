import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

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

# За 2014 год предоставить список самых популярных исходов спортивных событий (футбол) – 
# на которые игроки чаще всего делали свои ставки
# Представить в виде списка 10 самых популярных исходов, по уменьшению кол-ва сделанных ставок
soccer_data = df[df['SPORTS_NAME'] == 'Soccer']

soccer_selection_counts = soccer_data['SELECTION'].value_counts().to_dict()

top_10_soccer_selections = dict(sorted(soccer_selection_counts.items(), key=lambda item: item[1], reverse=True)[:10])

selection_names = [key for key, _ in top_10_soccer_selections.items()]
bets_counts = [value for _, value in top_10_soccer_selections.items()]

plt.figure(figsize=(12, 6))

x_positions = range(len(selection_names))

plt.plot(x_positions, bets_counts, 
         marker='o',
         markersize=10,
         linestyle='-',
         linewidth=2,
         color='#1f77b4',
         alpha=0.7)

i = 0

for selection_name, bets_count in top_10_soccer_selections.items():
    # Подпись значения
    plt.text(i, bets_count + 0.02, f"{bets_count}",
             ha='center', 
             va='bottom',
             fontsize=10,
             bbox=dict(facecolor='white', alpha=0.8))
    
    plt.text(i, -5, str(selection_name),
             ha='center',
             va='top',
             fontsize=10,
             rotation=45)
    
    i += 1

# Настройки осей
plt.xticks(x_positions, [])  # Убираем стандартные подписи
plt.xlabel("Название исхода", fontsize=12)
plt.ylabel("Кол-во ставок", fontsize=12)
plt.title("Футбольные исходы спортивных событий, на которые игроки чаще всего делали ставки", fontsize=14)

plt.ylim(min(bets_counts) * 0.9, max(bets_counts) * 1.1)
plt.xlim(-0.5, len(selection_names)-0.5)

plt.grid(True, linestyle='--', alpha=0.7)

plt.tight_layout()
plt.show()