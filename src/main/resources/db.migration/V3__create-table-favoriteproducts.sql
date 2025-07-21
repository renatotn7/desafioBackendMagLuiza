CREATE TABLE favorite_products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- ID único para o registro de favorito
    client_id UUID NOT NULL,                      -- ID do cliente que favoritou o produto
    product_id BIGINT NOT NULL,                   -- ID do produto da FakeStoreAPI (BIGINT pois a FakeStoreAPI usa IDs numéricos)
    title VARCHAR(255) NOT NULL,                  -- Título do produto (para guardar no local)
    image VARCHAR(256),                           -- URL da imagem do produto (para guardar no local)
    price NUMERIC(10, 2) NOT NULL,                -- Preço do produto (para guardar no local)
    review NUMERIC(2, 1),                         -- Review/avaliação do produto (ex: 4.5)

    -- Chave estrangeira para a tabela de clientes
    CONSTRAINT fk_client
        FOREIGN KEY (client_id)
        REFERENCES clients (id)
        ON DELETE CASCADE, -- Se um cliente for excluído, seus favoritos também são
    
    -- Restrição de unicidade para garantir que um cliente não favorite o mesmo produto múltiplas vezes
    CONSTRAINT uq_client_product UNIQUE (client_id, product_id)
);