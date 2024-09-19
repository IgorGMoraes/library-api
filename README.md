# Relatório do Projeto: API de Livros

## I. Arquitetura de Solução e Arquitetura Técnica

### Solução Implementada

O objetivo do projeto foi desenvolver uma API para uma livraria independente, permitindo que os clientes pesquisassem livros por gênero e autor, proporcionando uma experiência de navegação simples e eficiente.

### Tecnologias Usadas
- **Linguagem**: Java 21
- **Framework**: Spring Boot
- **Banco de Dados**: PostgreSQL
- **Migration**: Flyway
- **Cache**: Redis
- **Conteinerização**: Docker
- **Controle de Acesso**: Implementação de roles básicas (usuário e admin) com Spring Security.
- **Teste**: Junit5, Mockito, PowerMock

### Decisões de Design
1. **Banco de Dados**: PostgreSQL foi escolhido por sua estrutura relacional estável para o esquema de livros, podendo ser escalável usando réplicas.
2. **Cache**: Foi escolhido o Redis por ele oferecer mais funcionalidades, como persistência de dados, suporte a estruturas de dados mais complexas (considerei utilizá-lo na busca de últimos libvos acessados) e melhor escalabilidade que Memcached.
3. **Aqiusição dos dados**: Foi utilizada a biblioteca OpenCSV para simplificar e automatizar a importação de dados em massa a partir de um arquivo CSV contendo as informações dos livros.
4. **Spring Boot**: A escolha de Spring Boot se deu pela sua facilidade de integração com diferentes bibliotecas e frameworks e estrutura de camadas:
-  **Entity**: Representa a estrutura dos dados do sistema. Cada classe de entidade corresponde a uma tabela no banco de dados, contendo atributos que refletem as colunas da tabela.

-   **Repository**: É responsável pela interação com a base de dados. Utiliza interfaces que estendem `JpaRepository`, permitindo operações CRUD (Create, Read, Update, Delete) sem a necessidade de implementar métodos complexos.

-   **Service**: Contém a lógica de negócio do aplicativo. É onde as regras de negócio são implementadas e as operações são coordenadas, usando os repositórios para acessar os dados. O serviço pode agregar dados de diferentes repositórios ou aplicar validações.

-   **Controller**: É a camada que lida com as requisições HTTP. Recebe as solicitações do cliente, chama os serviços correspondentes e retorna as respostas. Os controladores são responsáveis por definir as rotas e mapear os dados de entrada e saída.

---

## II. Explicação sobre o Case Desenvolvido (Plano de Implementação)

### Lógica de Negócio

A API oferece endpoints para permitir que os usuários pesquisem livros por gênero e autor, com a possibilidade de futuras extensões para incluir funcionalidades como recomendação de livros com base em preferências do usuário.

### Estrutura de Dados
A entidade principal, **Books**, contém os seguintes atributos:
- Id
- Título
- Autor
- Gênero
- Preço
- Avaliação
- Quantidade de avaliações

Foi implementado um sistema simples de controle de acesso baseado em roles, com permissões diferenciadas para usuários comuns e administradores.

### Fluxo de Implementação

1. **Criação da API**:
-   Endpoints principais:
    -   Listar livros: `/book` com paginação de tamanho 10 por padrão.
    -   Buscar livro por identificador: `/book/{id}`.
    -   Buscar livros por gênero: `/book/genre/{genre}`.
    -   Buscar livros por autor: `/book/author/{author}`.
    -   Adicionar notas aos livros (restrito ao admin): `/book/{id}/rate?rate={rate}`.
    -   Listar os livros acessados recentemente: `/book/recent` (com armazenamento dos dados na sessão).
    -   Buscar os livros por título: `/book/title/{title}`. Caso o livro buscado não seja encontrado a aplicação irá buscar na Open Library Search API, trazendo os 20 primeiros livros encontrados'
    
2. **Banco de Dados**: A estrutura do banco de dados foi desenvolvida com PostgreSQL, aproveitando suas capacidades de modelagem relacional, principalmente devido à constância da estrutura de um livro. Cada livro está relacionado a um autor e a um gênero, permitindo consultas eficientes.

3. **Segurança**: Após toda a implemetação funcional da API, foi adicionada a camada de securança com autenticaçÃo e autorização dos endpoints baseados em roles (USER e ADMIN).

4. **Contenerização**: Depois de tudo implementado e testado, foi utilizada a biblioteca do Google jib para criação da imagem Docker de forma automatizada, utilizando um repoistório criado no Dockerhub (igorgmoraes/library-api) para armazenamento da imagem da api.

---

## III. Melhorias e Considerações Finais

### Possíveis Melhorias
1. **Recomendações Personalizadas**: Um próximo passo seria adicionar um sistema de recomendação de livros utilizando algoritmos de machine learning, ou integrando APIs como o ChatGPT para sugerir livros com base nas preferências do usuário.

2. **Segurança Avançada**: O controle de acesso foi implementado de forma simples, utilizando roles básicas. No futuro, seria interessante aprofundar o uso de OAuth 2.0 e OpenID Connect para uma autenticação mais robusta.

3. ~~**Integração com API Pública**: Integração com uma API pública para obter informações adicionais sobre livros, caso o catálogo da livraria precise de mais dados.~~ (implementado)

4. **Monitoramento**: Grafana e Prometheus podem ser integrados para fornecer métricas de uso da API e monitorar a performance dos serviços.

### Desafios Encontrados
- A escolha entre PostgreSQL e MongoDB apresentou uma reflexão sobre as necessidades atuais da aplicação versus o planejamento para crescimento futuro.
- Trabalhr com cache, por algum motivo o endpoint de retornar um livro por ID havia ficado com erro serialização e a anotação `@Cacheable` ficou sem a `key = "#id"`, após algumas pesquisas e investigação no código o problema foi resolvido 
- Após subir a aplicação no docker os endpoints não ficaram mais acessáveis, mesmo fazendo o login corretamente dos usuários. Também pode ser alguma configuração errada, talvez relacionada ao CORS.
- Desde a última vez que implementei autorização e autenticação com Spting Seurity, algumas coisas mudaram no framework e não pode ser feito da mesma maneira, mas obtive sucesso depois de me atualizar, buscando a documentação e tutoriais para poder implementar a camada de segurança.

## Inicializar a aplicação

### Pré-requisitos

Certifique-se de ter o **Docker** e **Docker Compose** instalados em sua máquina.

1.  Baixe o arquivo `docker-compose.yml` e execute o comando `docker-compose up` no terminal;

    `docker-compose up`

2.  Após o Docker Compose inicializar todos os contêineres, a API estará disponível no seguinte endereço:

    `http://localhost:8080`

#### Observações

-   A aplicação estará conectada ao banco de dados PostgreSQL, acessível internamente pela URL `jdbc:postgresql://postgres:5432/library`.
-   Para detalhes dos enpoints basta acessar `http://localhost:8080/swagger-ui`
