install:
	mvn -B dependency:resolve

build:
	mvn -B package -DskipTests

test:
	mvn -B test

test-unit:
	mvn -B test -Dtest="**/unit/**"

test-integration:
	mvn -B test -Dtest="**/integration/**"

verify:
	mvn -B verify

check: verify
