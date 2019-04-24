package com.example.representuapp;

public class HashMap {

    // better re-sizing is taken as 2^4
    private final int[] SIZE = {16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384};
    private Entry table[] = new Entry[SIZE[0]];
    private int fill = 0;
    private int index = 0;

    /**
     * To store the Map data in key and value pair.
     * Using linear probing by 1
     */
    class Entry {
        final String key;
        String value;
        boolean collided;

        Entry(String k, String v) {
            key = k;
            value = v;
            collided = false;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }
    }

    /**
     * Returns the entry mapped to key in the HashMap.
     */
    public Entry get(String k) {
        int hash = Math.abs(mod(k.hashCode(), SIZE[index]));
        Entry e = table[hash];
        if (e == null) {
            return null;
        }else if (e.collided && !e.key.equals(k)) {
            //split for loops for efficiency
            for (int i = hash; i < SIZE[index]; i++) {
                if (table[i].key.equals(k)) {
                    return table[i];
                }
            }
            for (int i = 0; i < hash; i++) {
                if (table[i].key.equals(k)) {
                    return table[i];
                }
            }
            return null;

        } else if (e.key.equals(k)) { return e; }

        return null;
    }

    /** to help with hash **/
    private int mod(int x, int y) {
        int result = x % y;
        if (result < 0)
            result += y;
        return result;
    }

    /**
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     * If there is a collision, original Entry marked, hash increases until
     * no collisions, places new Entry
     */
    public void put(String k, String v) {
        int hash = Math.abs(mod(k.hashCode(),SIZE[index]));
        table = resize(table);
        Entry e = table[hash];

        if(e != null) {
            if(e.key.equals(k)) {
                e.value = v;
            } else {
                // Collision, probe
                e.collided = true;
                while (table[hash] != null) {
                    if (hash + 1 < SIZE[index]) {
                        hash++;
                    } else { hash = 0; }
                }
                e = new Entry(k, v);
                table[hash]= e;
                fill++;
            }
        } else {
            table[hash]= new Entry(k, v);
            fill++;
        }
    }

    private Entry[] resize(Entry[] table) {
        //if table has filled up
        if (fill + 1 >= SIZE[index]) {
            //create a new one
            Entry new_table[] = new Entry[SIZE[index + 1]];
            //populate it
            for (int i = 0; i <= SIZE[index]; i++) {
                new_table[i] = table[i];
            }
            //increase index
            index++;
            return new_table;
        }
        return table;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (int i = 0; i < SIZE[index]; i++){
            if (table[i] != null) {
                sb.append("(k:");
                if (table[i].key == null) {
                    sb.append("null");
                } else {
                    sb.append(table[i].key);
                }
                sb.append(" v:");
                if (table[i].value == null) {
                    sb.append("null");
                } else {
                    sb.append(table[i].value);
                }
                sb.append("), ");
            } else {
                sb.append("(NULL), ");
            }
        }
        sb.append(" }");
        return sb.toString();
    }
}
