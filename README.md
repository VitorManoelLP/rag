# Spring Boot RAG Assistant

## 📝 Sobre o Projeto

Este projeto implementa um sistema RAG (Retrieval-Augmented Generation) utilizando Spring Boot e PostgreSQL. O objetivo é criar uma assistente digital capaz de analisar documentos PDF e fornecer insights relevantes através de processamento de linguagem natural.

## 🚀 Funcionalidades

- Upload e processamento de documentos PDF
- Extração automática de texto em chunks usando iText
- Geração de embeddings vetoriais do texto
- Armazenamento eficiente usando pgvector no PostgreSQL
- Sistema de busca por similaridade vetorial
- Interface de conversação com IA para análise contextual

## 🛠️ Como Funciona

1. **Upload do Documento**
   - O usuário faz upload de um arquivo PDF
   - O sistema extrai o texto usando iText
   - O texto é segmentado em chunks menores
   - Embeddings são gerados para cada chunk
   - Os vetores são armazenados no PostgreSQL usando pgvector

2. **Processo de Consulta**
   - O usuário faz uma pergunta sobre o documento
   - A pergunta é convertida em embedding
   - O sistema busca os 5 chunks mais similares no banco
   - Um prompt contextualizado é gerado
   - A IA responde baseada no contexto encontrado

## 🔧 Tecnologias Utilizadas

- Spring Boot
- PostgreSQL com pgvector
- iText PDF
- Spring AI
- LLM para geração de respostas

## 📋 Pré-requisitos

- Java 21
- Angular 19
- Docker

## 🚀 Como Executar

- Na pasta frontend/rag, executar `npm i` e posteriormente `npm run start`.
- Na raiz do projeto, executar `docker compose up`.
- Na pasta backend, execute o comando `OPENAI_API_KEY={SUA_CHAVE_OPENAI} ./gradlew bootRun` e substitua {SUA_CHAVE_OPENAI} pela sua própria chave.
