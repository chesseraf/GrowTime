import sys
import re

PLANT_NAMES = ['Tulip (indoor)', 'Rose (3 days)', 'Cactus']  # maps a, b, c respectively

RAIN_MM_PER_DAY = [
    0.0,  2.5,  10.0, 0.0,  0.0,
    3.0,  0.0,  5.0,  0.0,  0.0,
    0.0,  0.0,  5.0,  0.0,  0.0,
    12.0, 0.0,  4.0,  0.0,  0.0,
]


def rain_level(mm):
    if mm <= 0:
        return 0
    return 1 if mm < 6 else 2  # 1=yellow (light), 2=red (heavy)


def parse_log(log_path, output_path):
    with open(log_path, 'r') as f:
        lines = f.readlines()

    non_empty = [l.rstrip() for l in lines if l.strip()]
    last_20 = non_empty[-20:]

    watered_by_day = {}
    for line in last_20:
        # Match ): DAY : plants  (new format)  or  BUFF    : DAY : plants  (old format)
        m = re.search(r'(?:\d+\)|output\s*):\s*(\d+)\s*:\s*(.*)', line)
        if not m:
            continue
        day = int(m.group(1))
        plants_raw = m.group(2).strip()
        plants = {p.strip() for p in re.split(r'[,\s]+', plants_raw) if p.strip()}
        watered_by_day[day] = plants

    with open(output_path, 'w', newline='') as f:
        f.write(','.join(PLANT_NAMES) + ',Rain\n')
        for day in range(20):
            watered = watered_by_day.get(day, set())
            row = ['1' if letter in watered else '0' for letter in 'abc']
            mm = RAIN_MM_PER_DAY[day] if day < len(RAIN_MM_PER_DAY) else 0.0
            row.append(str(rain_level(mm)))
            f.write(','.join(row) + '\n')

    print(f"Written to {output_path}")


if __name__ == '__main__':
    log_file = sys.argv[1] if len(sys.argv) > 1 else '../app_logs.txt'
    out_file = sys.argv[2] if len(sys.argv) > 2 else 'actual_data.csv'
    parse_log(log_file, out_file)
