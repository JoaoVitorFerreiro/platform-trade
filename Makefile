.PHONY: help build up down logs test clean

help:
	@echo "Comandos disponíveis:"
	@echo "  make build    - Build da aplicação"
	@echo "  make up       - Subir aplicação completa"
	@echo "  make down     - Parar aplicação"
	@echo "  make logs     - Ver logs da aplicação"
	@echo "  make test     - Rodar testes"
	@echo "  make clean    - Limpar volumes e containers"

build:
	docker-compose build

up:
	docker-compose up -d

down:
	docker-compose down

logs:
	docker-compose logs -f app

test:
	docker-compose -f docker-compose.test.yml up --build --abort-on-container-exit
	docker-compose -f docker-compose.test.yml down -v

clean:
	docker-compose down -v
	mvn clean