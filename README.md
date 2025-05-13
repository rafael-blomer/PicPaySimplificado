# 📱 PicPay Simplificado

## 📝 Resumo

Este projeto foi desenvolvido como um desafio de backend proposto pelo PicPay. Meu principal objetivo foi testar meus conhecimentos e avaliar meu nível técnico, com a meta de concluir o desenvolvimento em até **7 dias**.

A aplicação consiste em uma **API RESTful** que simula um banco digital simplificado, com as seguintes funcionalidades principais:

- Registro de usuários do tipo **Lojista** e **Comum**;
- Realização de **transferências** entre usuários (usuários do tipo lojista **apenas recebem**, não podem enviar);
- Execução de **depósitos** em contas de usuários;
- Alteração de **senha** da conta;
- Consulta de dados de **um ou todos os usuários**;
- Não permitir que usuários tenham o mesmo email ou documento(CPF/CNPJ);
- **Exclusão** de usuários;
- Integração com APIs externas:
  - Uma API de **autorização de transações**;
  - Uma API de **notificação de transferência**.

## 🚧 Principais Desafios

Durante o desenvolvimento do projeto, enfrentei alguns desafios importantes que contribuíram bastante para o meu aprendizado:

### 🔗 Integração com OpenFeign
Já tinha um conhecimento básico da ferramenta, mas precisei estudar um pouco mais para utilizá-la corretamente na comunicação com APIs externas.

### 🐳 Dockerização da aplicação
Foi necessário pesquisar para aprender a **dockerizar** a aplicação adequadamente, o que foi um ponto de evolução pessoal.

### 🧩 Estrutura de Herança entre Usuários
Inicialmente, optei por criar uma classe pai `Usuario` e estender `UsuarioLojista` e `UsuarioComum`, com apenas um `repository`, `service` e `controller`. Porém, com o avanço do projeto, percebi que o ideal seria separar essas classes devido a **regras de negócio específicas** e **atributos distintos**.  
Essa refatoração ficou pendente por conta do tempo limitado. Se tivesse utilizado **TDD (Test-Driven Development)** desde o início, provavelmente teria identificado essa necessidade mais cedo.

### 🗂️ Regras de Exclusão de Usuário
Outro desafio foi lidar com a **exclusão de usuários envolvidos em transações**. Para evitar falhas de integridade no banco de dados, precisei executar os seguintes comandos diretamente no PostgreSQL:
```
ALTER TABLE transacao DROP CONSTRAINT fk_usuario_remetente;

ALTER TABLE transacao ADD CONSTRAINT fk_usuario_remetente FOREIGN KEY
(usuario_remetente) REFERENCES usuario(id) ON DELETE SET NULL;

ALTER TABLE transacao DROP CONSTRAINT fk_usuario_destinatario;

ALTER TABLE transacao ADD CONSTRAINT fk_usuario_destinatario FOREIGN KEY
(usuario_destinatario) REFERENCES usuario(id) ON DELETE SET NULL;
```
Se tivesse utilizado **TDD**, esse problema poderia ter sido identificado e tratado de forma mais elegante e precoce no código.

## 🚀 Aprendizados

Apesar das dificuldades, cada desafio enfrentado me ajudou a evoluir como desenvolvedor, tornando-me mais atento aos detalhes e à importância de boas práticas como TDD e uma arquitetura bem planejada desde o início.

## 🛠️ Tecnologias Utilizadas

- ☕ Java 21  
- 🌱 Spring Boot  
- 🧪 JUnit  
- 🐘 PostgreSQL  
- 🐳 Docker  
- 🧰 OpenFeign  
- 📮 Postman  
- 🗃️ JPA (Java Persistence API)


## 📬 Link do desafio
[Desafio PicPay Backend](https://github.com/PicPay/picpay-desafio-backend)

