CREATE VIEW LoginDB AS
SELECT 
    DoctorID AS ID,
    PassWord AS Password,
    'Doctor' AS Role
FROM 
    DB2024_Doctor
UNION ALL
SELECT 
    NurseID AS ID,
    PassWord AS Password,
    'Nurse' AS Role
FROM 
    DB2024_Nurse;