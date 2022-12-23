#!/bin/bash

find metadata -name '*.txt' -exec sed -Ei 's/^[–—─•·*]\s+/- /' {} \;
