ALTER TABLE projeto DROP CONSTRAINT projeto_cliente_id_fkey;
ALTER TABLE projeto ADD CONSTRAINT projeto_cliente_id_fkey FOREIGN KEY (cliente_id) REFERENCES cliente(id);
