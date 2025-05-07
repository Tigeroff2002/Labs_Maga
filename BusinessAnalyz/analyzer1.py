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

# Предоставить список самых популярных видов спорта – согласно тому, сколько ставок было сделано на проведенные события этого вида спорта
sports_bets_counts = df["SPORTS_NAME"].value_counts().to_dict()

sports_bets_counts_sorted = dict(sorted(
    sports_bets_counts.items(),
    key=lambda item: item[1],
    reverse=True
))

sports = list(sports_bets_counts_sorted.keys())
bets_number = list(sports_bets_counts_sorted.values())

plt.figure(figsize=(12, 6))

bars = plt.bar(sports, bets_number, color="skyblue")

plt.xlabel("Вид спорта", fontsize=12)
plt.ylabel("Количество ставок", fontsize=12)
plt.title("Распределение ставок по видам спорта", fontsize=14, pad=20)

plt.xticks(rotation=45, ha="right") 

for bar in bars:
    height = bar.get_height()
    plt.text(
        bar.get_x() + bar.get_width() / 2,  # X-координата надписи
        height + 0.5,                       # Y-координата (немного выше столбца)
        f"{int(height)}",                    # Текст (число ставок)
        ha="center",                        # Горизонтальное выравнивание
        va="bottom",                        # Вертикальное выравнивание
        fontsize=10
    )

plt.tight_layout()

plt.show()
