## Usage

Run ``$ gradle jmh``

## Benchmark results (ops/sec)

|       | Integer.parseInt  | Character.isDigit  | String.matches |
|-------|-------------------|--------------------|----------------|
| Score |      653152.762   |    26427804.312    |   2676144.671  |
| Error |    16139.468      |     133216.340     |    24295.376   |
