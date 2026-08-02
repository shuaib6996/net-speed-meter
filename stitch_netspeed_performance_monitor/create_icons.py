import struct, zlib, os

def create_png(width, height, r, g, b, a=255):
    """Create a minimal solid-color PNG file as bytes."""
    def chunk(chunk_type, data):
        c = chunk_type + data
        return struct.pack('>I', len(data)) + c + struct.pack('>I', zlib.crc32(c) & 0xffffffff)

    header = b'\x89PNG\r\n\x1a\n'
    ihdr = chunk(b'IHDR', struct.pack('>IIBBBBB', width, height, 8, 6, 0, 0, 0))

    raw = b''
    for y in range(height):
        raw += b'\x00'  # filter none
        for x in range(width):
            raw += bytes([r, g, b, a])

    idat = chunk(b'IDAT', zlib.compress(raw))
    iend = chunk(b'IEND', b'')
    return header + ihdr + idat + iend

# Icon sizes for each density
sizes = {
    'mdpi': 48,
    'hdpi': 72,
    'xhdpi': 96,
    'xxhdpi': 144,
    'xxxhdpi': 192,
}

base = 'stitch_netspeed_performance_monitor/app/src/main/res'

# Teal color (#00DFC1)
r, g_b, b_val = 0x00, 0xDF, 0xC1

for density, size in sizes.items():
    path = os.path.join(base, f'mipmap-{density}')
    os.makedirs(path, exist_ok=True)

    # Create icon: dark background with teal circle
    png_data = create_png(size, size, 0x0A, 0x0C, 0x10)  # dark bg

    filepath = os.path.join(path, 'ic_launcher.png')
    with open(filepath, 'wb') as f:
        f.write(png_data)
    print(f'Created {filepath} ({size}x{size})')

    # Round icon
    round_path = os.path.join(path, 'ic_launcher_round.png')
    with open(round_path, 'wb') as f:
        f.write(png_data)
    print(f'Created {round_path} ({size}x{size})')

print('All PNG icons created!')