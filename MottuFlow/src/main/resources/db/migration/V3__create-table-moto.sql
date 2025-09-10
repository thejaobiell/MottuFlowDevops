CREATE TABLE moto (
    id_moto BIGINT PRIMARY KEY AUTO_INCREMENT,
    placa VARCHAR(10) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    fabricante VARCHAR(50) NOT NULL,
    ano INT NOT NULL,
    id_patio BIGINT NOT NULL,
    localizacao_atual VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_patio) REFERENCES patio(id_patio)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

INSERT INTO moto (placa, modelo, fabricante, ano, id_patio, localizacao_atual) VALUES
('ABC-1234', 'CG 160', 'Honda', 2022, 1, 'Vaga 01'),
('DEF-5678', 'XRE 300', 'Honda', 2021, 1, 'Vaga 02'),
('GHI-9012', 'Ténéré 250', 'Yamaha', 2023, 2, 'Vaga 01'),
('JKL-3456', 'Ninja 400', 'Kawasaki', 2022, 3, 'Vaga 03');