import pandas as pd
import matplotlib.pyplot as plt
import sys
import os

def plot_watering_chart(csv_path, expected_csv_path):
    df = pd.read_csv(csv_path)
    df_exp = pd.read_csv(expected_csv_path)

    # Last column is rain, all others are plants
    plant_cols = df.columns[:-1].tolist()
    rain_col = df.columns[-1]

    days = list(range(1, len(df) + 1))

    # Color palette for plants
    plant_colors = ['#4CAF50', '#5C85D6', '#D4A017', '#9C59B6', '#E67E22', '#1ABC9C']

    fig, ax = plt.subplots(figsize=(8, 5))

    n = len(plant_cols)
    # Spread plants evenly across a small vertical band so no two overlap
    offset_range = 0.12
    offsets = [0.0] if n == 1 else [
        -offset_range / 2 + offset_range * i / (n - 1) for i in range(n)
    ]

    for i, plant in enumerate(plant_cols):
        color = plant_colors[i % len(plant_colors)]
        y = [v + offsets[i] for v in df[plant]]

        # Draw the line connecting all points (no markers on the line itself)
        ax.plot(days, y, color=color, linewidth=2, label=plant, zorder=2)

        # Draw each point individually: circle if correct, X if mismatch
        for j, day in enumerate(days):
            actual = df[plant].iloc[j]
            expected = df_exp[plant].iloc[j]
            matches = (actual == expected)

            if matches:
                ax.plot(day, y[j], marker='o', color=color, markersize=7,
                        markeredgecolor='white', markeredgewidth=1.5, zorder=4)
            else:
                ax.plot(day, y[j], marker='x', color=color, markersize=9,
                        markeredgewidth=2.5, zorder=4)

    # Plot rain dots just below y=0 for days it rained
    rain_days = [days[i] for i in range(len(df)) if df[rain_col].iloc[i]]
    ax.scatter(rain_days, [-0.1] * len(rain_days), color='red', zorder=5, s=60, label='Rain', clip_on=False)

    # Styling
    ax.set_title('Watering Notification Accuracy Simulation', fontsize=13, pad=10)
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

    # Legend: plants + rain + mismatch indicator
    handles, labels = ax.get_legend_handles_labels()
    mismatch_marker = plt.Line2D([0], [0], marker='x', color='gray', linestyle='None',
                                  markersize=8, markeredgewidth=2.5, label='Mismatch')
    handles.append(mismatch_marker)
    ax.legend(handles=handles, loc='upper center', bbox_to_anchor=(0.5, 1.0),
              ncol=len(plant_cols) + 2, fontsize=9, frameon=False)

    plt.tight_layout()

    out_path = os.path.splitext(csv_path)[0] + '_chart.png'
    plt.savefig(out_path, dpi=150, bbox_inches='tight')
    print(f"Chart saved to: {out_path}")
    plt.show()


if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("Usage: python watering_chart.py <actual.csv> <expected.csv>")
        print()
        print("Both CSVs must have the same structure:")
        print("  - One column per plant (e.g. Tulip, Cactus, Rose)")
        print("  - Last column in actual.csv: Rain (1 = rained, 0 = no rain)")
        print("  - Each row = one day; 1 = notification triggered, 0 = not")
        print()
        print("Points are shown as circles (correct) or X (mismatch vs expected).")
        sys.exit(1)

    plot_watering_chart(sys.argv[1], sys.argv[2])