CREATE TABLE clients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Uses UUID for the ID, automatically generated
    name VARCHAR(80) NOT NULL,                    -- Client's name, cannot be null
    email VARCHAR(60) UNIQUE NOT NULL,                     -- Client's email, must be unique
    cell_phone VARCHAR(25),                        -- Cell phone number (flexible format)
    street_address VARCHAR(255),                   -- Street name, avenue, etc.
    address_complement VARCHAR(80),               -- Address complement (apt, block, etc.)
    neighborhood VARCHAR(100),
    city VARCHAR(100),
    uf VARCHAR(2),                              -- State abbreviation (e.g., RJ, SP)
    zip_code VARCHAR(10),                          -- Zip Code (e.g., 90210-1234)
    phone VARCHAR(20)                            -- Landline phone (flexible format)
);

-- Optional: Add an index for the email field to optimize searches
CREATE INDEX idx_clients_email ON clients (email);

