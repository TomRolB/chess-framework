# Cannot move one by one

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
3. b2-b3


# Result
`LAST_MOVE_INVALID`

# Final board
```
   a  b  c  d  e  f  g  h
1 |WR|WN|WB|WQ|WK|WB|WN|WR|
2 |  |WP|WP|WP|WP|WP|WP|WP|
3 |  |  |  |  |  |  |  |  |
4 |WP|  |  |  |  |  |  |  |
5 |  |BP|  |  |  |  |  |  |
6 |  |  |  |  |  |  |  |  |
7 |BP|  |BP|BP|BP|BP|BP|BP|
8 |BR|BN|BB|BQ|BK|BB|BN|BR|
```