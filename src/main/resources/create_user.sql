CREATE USER IF NOT EXISTS admin WITH PASSWORD '1234' CREATEDB;

GRANT ALL ON DATABASE inventory_management TO admin;   
GRANT ALL PRIVILEGES ON DATABASE inventory_management TO admin;
