#!/bin/sh
set -eu
ROOT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
set -a
. "$ROOT_DIR/.env"
set +a
cd "$ROOT_DIR/backend"
exec ./mvnw spring-boot:run
