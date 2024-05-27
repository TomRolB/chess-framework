# Moving pawns incrementally

# Size
width = 8
height = 8

# Starting board
```
    a  b  c  d  e  f  g  h
1 |WR|WN|WB|WQ|WK|WB|WN|WR|
2 |WP|WP|WP|WP|WP|WP|WP|WP|
3 |  |  |  |  |  |  |  |  |
4 |  |  |  |  |  |  |  |  |
5 |  |  |  |  |  |  |  |  |
6 |  |  |  |  |  |  |  |  |
7 |BP|BP|BP|BP|BP|BP|BP|BP|
8 |BR|BN|BB|BQ|BK|BB|BN|BR|
```
# Moves
1. a2-a4
2. b7-b5
3. a7-a5
4. b2-b3
5. c2-c3
6. d2-d4

# Result
`ALL_MOVES_VALID`

# Final board
```
   a  b  c  d  e  f  g  h
1 |WR|WN|WB|WQ|WK|WB|WN|WR|
2 |  |  |  |  |WP|WP|WP|WP|
3 |  |WP|WP|  |  |  |  |  |
4 |WP|  |  |WP|  |  |  |  |
5 |BP|BP|  |  |  |  |  |  |
6 |  |  |  |  |  |  |  |  |
7 |  |  |BP|BP|BP|BP|BP|BP|
8 |BR|BN|BB|BQ|BK|BB|BN|BR|
```