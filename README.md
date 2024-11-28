sql command to build the table for this project:

```sql
CREATE TABLE transaction_records (
    transactionId BIGINT NOT NULL,
    accountNumber BIGINT NOT NULL,
    remarks VARCHAR(255),
    deposit DECIMAL(10, 2),
    withdraw FLOAT,
    balance FLOAT,
    PRIMARY KEY (transactionId, accountNumber),
    CONSTRAINT fk_accountNumber FOREIGN KEY (accountNumber) REFERENCES users(accountNumber)
);

CREATE TABLE users (
    fullName VARCHAR(50) DEFAULT NULL,
    dob VARCHAR(10) DEFAULT NULL,
    address VARCHAR(100) DEFAULT NULL,
    pan VARCHAR(10) DEFAULT NULL,
    aadhar VARCHAR(12) DEFAULT NULL,
    phone VARCHAR(10) DEFAULT NULL,
    isMarried INT DEFAULT NULL,
    isEmp INT DEFAULT NULL,
    gender VARCHAR(20) DEFAULT NULL,
    email VARCHAR(50) DEFAULT NULL,
    username VARCHAR(50) DEFAULT NULL,
    password VARCHAR(100) DEFAULT NULL,
    accountNumber BIGINT DEFAULT NULL UNIQUE
);
```
