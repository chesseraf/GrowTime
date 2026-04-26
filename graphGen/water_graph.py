import pandas as pd
import matplotlib.pyplot as plt
import sys
import os

def plot_watering_chart(csv_path):
    df = pd.read_csv(csv_path)

    # Last column is rain, all others are plants
    plant_cols = df.columns[:-1].tolist()
    rain_col = df.columns[-1]

    days = list(range(1, len(df) + 1))

    plant_colors = ['#4CAF50', '#5C85D6', '#D4A017', '#9C59B6', '#E67E22', '#1ABC9C']

    fig, ax = plt.subplots(figsize=(8, 5))

    n = len(plant_cols)
    offset_range = 0.12
    offsets = [0.0] if n == 1 else [
        -offset_range / 2 + offset_range * i / (n - 1) for i in range(n)
    ]

    for i, plant in enumerate(plant_cols):
        color = plant_colors[i % len(plant_colors)]
        y = [v + offsets[i] for v in df[plant]]

        ax.plot(days, y, color=color, linewidth=2, label=plant, zorder=2)

        for j, day in enumerate(days):
            ax.plot(day, y[j], marker='o', color=color, markersize=7,
                    markeredgecolor='white', markeredgewidth=1.5, zorder=4)

    # Plot rain dots just below y=0: yellow = light (<6mm), red = heavy (>=6mm)
    light_days = [days[i] for i in range(len(df)) if df[rain_col].iloc[i] == 1]
    heavy_days = [days[i] for i in range(len(df)) if df[rain_col].iloc[i] == 2]
    if light_days:
        ax.scatter(light_days, [-0.1] * len(light_days), color='gold', zorder=5, s=60,
                   label='Light Rain', clip_on=False)
    if heavy_days:
        ax.scatter(heavy_days, [-0.1] * len(heavy_days), color='red', zorder=5, s=60,
                   label='Heavy Rain', clip_on=False)

    ax.set_title('Watering Notification Simulation', fontsize=13, pad=10)
    ax.set_xlabel('Time (Days)', fontsize=11)
    ax.set_ylabel('Notification triggered', fontsize=10)

    ax.set_xticks(days)
    ax.set_yticks([0, 1])
    ax.set_yticklabels(['No', 'Yes'])
    ax.set_xlim(0.5, len(days) + 0.5)
    ax.set_ylim(-0.25, 1.35)

    ax.spines['top'].set_visible(False)
    ax.spines['right'].set_visible(False)
    ax.grid(axis='y', linestyle='-', linewidth=0.5, alpha=0.4)

    handles, labels = ax.get_legend_handles_labels()
    ax.legend(handles=handles, loc='upper center', bbox_to_anchor=(0.5, 1.0),
              ncol=len(handles), fontsize=9, frameon=False)

    plt.tight_layout()

    out_path = os.path.splitext(csv_path)[0] + '_chart.png'
    plt.savefig(out_path, dpi=150, bbox_inches='tight')
    print(f"Chart saved to: {out_path}")
    plt.show()


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("Usage: python water_graph.py <actual.csv>")
        sys.exit(1)

    plot_watering_chart(sys.argv[1])
