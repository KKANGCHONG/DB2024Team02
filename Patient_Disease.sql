SELECT
    Name,
    (SELECT DiseaseName
     FROM DB2024_Disease d
     WHERE d.DiseaseID = 
           (SELECT DiseaseID
            FROM DB2024_Treatment t
            WHERE t.PatientID = p.PatientID
            LIMIT 1)
    ) AS DiseaseName
FROM
    DB2024_Patient p;
