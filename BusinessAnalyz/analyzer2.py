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

# За 2014 год предоставить список сделанных ставок для выбранного спорта (футбол) – 
# у которых отклонение коэффициента больше среднего коэффициента для типа исхода, на который делается ставка, 
# отсортировать ставки в порядке уменьшения отклонения коэффициента
df["ODDS"] = pd.to_numeric(df["ODDS"], errors="coerce")

df = df.dropna(subset=["ODDS"])

selection_avg_odds = df.groupby("SELECTION_ID")["ODDS"].mean().to_dict()

def get_odds_deviations(df, sports_name, selection_avg_odds):
    sport_df = df[df["SPORTS_NAME"] == sports_name].copy()
    
    sport_df["ODDS_DEVIATION"] = sport_df.apply(
        lambda row: abs(row["ODDS"] - selection_avg_odds[row["SELECTION_ID"]]),
        axis=1
    )
    
    deviations = sport_df["ODDS_DEVIATION"].to_dict()
    deviations_sorted = dict(sorted(deviations.items(), key=lambda x: x[1], reverse=True))
    
    return deviations_sorted

first_10_deviations_soccer = dict(list(get_odds_deviations(df, "Soccer", selection_avg_odds).items())[:10])

for bet_id, deviation in list(first_10_deviations_soccer.items())[:10]:
    print(f"ID ставки: {bet_id}, Отклонение: {deviation:.4f}")

bet_ids = [str(key) for key, _ in first_10_deviations_soccer.items()]  # ID как строки
deviations = [value for _, value in first_10_deviations_soccer.items()]    # Значения отклонений

plt.figure(figsize=(12, 6))

x_positions = range(len(bet_ids))

plt.plot(x_positions, deviations, 
         marker='o',
         markersize=10,
         linestyle='-',
         linewidth=2,
         color='#1f77b4',
         alpha=0.7)

i = 0

for bet_id, dev in first_10_deviations_soccer.items():
    # Подпись значения
    plt.text(i, dev + 0.02, f"{dev:.2f}",
             ha='center', 
             va='bottom',
             fontsize=10,
             bbox=dict(facecolor='white', alpha=0.8))
    
    plt.text(i, 1010, str(bet_id),
             ha='center',
             va='top',
             fontsize=10,
             rotation=45)
    
    i += 1

# Настройки осей
plt.xticks(x_positions, [])  # Убираем стандартные подписи
plt.xlabel("ID ставки", fontsize=12)
plt.ylabel("Величина отклонения", fontsize=12)
plt.title("Футбольные ставки с наивысшими отклонениями коэффициентов \n от медианных значения по исходу", fontsize=14)

plt.ylim(min(deviations) - 5, max(deviations) + 5)
plt.xlim(-0.5, len(bet_ids)-0.5)

plt.grid(True, linestyle='--', alpha=0.7)

plt.tight_layout()
plt.show()