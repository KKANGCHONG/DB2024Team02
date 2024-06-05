CREATE DATABASE DB2024Team02;
USE DB2024Team02;

-- 의사 테이블
CREATE TABLE DB2024_Doctor (
DoctorID INT PRIMARY KEY,
DoctorName VARCHAR(100) NOT NULL,
DoctorDepartment VARCHAR(50) NOT NULL,
ContactNumber VARCHAR(15) NOT NULL,

StartTime TIME,
EndTime TIME,

PassWord INT NOT NULL
);

-- 간호사 테이블
CREATE TABLE DB2024_Nurse (
NurseID INT PRIMARY KEY,
NurseName VARCHAR(100) NOT NULL,
NurseDepartment VARCHAR(50) NOT NULL,
ContactNumber VARCHAR(15) NOT NULL,

StartTime TIME,
EndTime TIME,

PassWord INT NOT NULL
);

-- 환자 테이블
CREATE TABLE DB2024_Patient (
PatientID INT PRIMARY KEY AUTO_INCREMENT,
Name VARCHAR(100) NOT NULL,
Birth DATE NOT NULL,
ResidentNum VARCHAR(15) NOT NULL,
Address VARCHAR(255) NOT NULL,
Phone VARCHAR(15),
GuardianPhone VARCHAR(15),

DoctorID INT NOT NULL DEFAULT 00000,
NurseID INT NOT NULL,
FOREIGN KEY (DoctorID) REFERENCES DB2024_Doctor(DoctorID) ON DELETE SET DEFAULT,
FOREIGN KEY (NurseID) REFERENCES DB2024_Nurse(NurseID) ON DELETE NO ACTION
);

-- 질병 테이블
CREATE TABLE DB2024_Disease (
DiseaseID INT PRIMARY KEY,
DiseaseName VARCHAR(100) NOT NULL
);

-- 약물 테이블
CREATE TABLE DB2024_Medication (
MedicationID INT PRIMARY KEY,
MedicationName VARCHAR(100) NOT NULL
);

-- 치료 테이블
CREATE TABLE DB2024_Treatment (
PatientID INT,
DiseaseID INT,

MedicationID INT,
RecommendedTreatment TEXT,
Date DATE,
Dosage VARCHAR(50) NOT NULL,

KTAS INT NOT NULL, 
PRIMARY KEY (PatientID, DiseaseID),
FOREIGN KEY (PatientID) REFERENCES DB2024_Patient(PatientID) ON DELETE NO ACTION,
FOREIGN KEY (DiseaseID) REFERENCES DB2024_Disease(DiseaseID) ON DELETE NO ACTION, 

FOREIGN KEY (MedicationID) REFERENCES DB2024_Medication(MedicationID)ON DELETE NO ACTION
);

INSERT INTO DB2024_Doctor (DoctorID, DoctorName, DoctorDepartment, ContactNumber, StartTime, EndTime, PassWord)
VALUES
(00000, 'Default Doctor', 'None', '000-0000-0000', '00:00:00', '00:00:00', 0000),
(91101, '김민준', '내과', '010-2397-2946', '08:00:00', '17:00:00', 1234),
(91102, '이서연', '내과', '010-3396-2017', '09:00:00', '18:00:00', 2345),
(91103, '박지훈', '내과', '010-2347-0193', '07:00:00', '16:00:00', 3456),
(91104, '최수민', '내과', '010-8093-8812', '08:30:00', '17:30:00', 4567),
(91201, '정예린', '신경과', '010-1209-4829', '09:30:00', '18:30:00', 5678),
(91202, '김현우', '신경과', '010-2397-0198', '10:00:00', '19:00:00', 6789),
(91301, '윤지아', '정형외과','010-8013-1938', '07:30:00', '16:30:00', 7890),
(91302, '강민서', '정형외과', '010-9133-2493', '08:30:00', '17:30:00', 8901),
(91401, '한서윤', '소아과', '010-2948-8491', '09:30:00', '18:30:00', 9012),
(91402, '서지훈', '소아과', '010-1039-9328', '08:00:00', '17:00:00', 1010);

INSERT INTO DB2024_Nurse (NurseID, NurseName, NurseDepartment, ContactNumber, StartTime, EndTime, PassWord)
VALUES
(21101, '김하영', '내과', '010-1394-4492', '08:00:00', '17:00:00', 1111),
(21102, '박수진', '내과', '010-7410-4940', '09:00:00', '18:00:00', 2222),
(21103, '이정민', '내과', '010-3491-2094', '07:00:00', '16:00:00', 3333),
(21104, '최유리', '내과', '010-8819-2910', '08:30:00', '17:30:00', 4444),
(21105, '정혜린', '내과', '010-5701-7293', '09:30:00', '18:30:00', 5555),
(21106, '김민서', '내과', '010-4819-8193', '10:00:00', '19:00:00', 6666),
(21201, '윤지우', '신경과', '010-3391-9213', '07:30:00', '16:30:00', 7777),
(21202, '강소연', '신경과', '010-4912-1923', '08:30:00', '17:30:00', 8888),
(21203, '한예진', '신경과', '010-9413-4193', '09:30:00', '18:30:00', 9999),
(21301, '서주희', '정형외과', '010-6577-3918', '08:00:00', '17:00:00', 1011),
(21302, '김지민', '정형외과', '010-8132-5701', '08:00:00', '17:00:00', 1112),
(21303, '박현아', '정형외과', '010-3871-9138', '09:00:00', '18:00:00', 1213),
(21401, '이서현', '소아과', '010-8813-9401', '07:00:00', '16:00:00', 1314),
(21402, '최나은', '소아과', '010-7661-9238', '08:30:00', '17:30:00', 1415),
(21403, '정지현', '소아과', '010-5671-0492', '09:30:00', '18:30:00', 1516);

INSERT INTO DB2024_patient (Name, Birth, ResidentNum, Address, Phone, GuardianPhone, DoctorID, NurseID)
VALUES
('김한나', '1990-12-04', '901204-2481049', '서울시 강남구', '010-5468-1846', '010-8866-8486', 91101, 21101),
('김철수', '1985-02-12', '850212-1012342', '부산시 해운대구', '010-4598-7811', '010-4468-1894', 91102, 21102),
('이영희', '1978-03-23', '780323-2409819', '서울시 송파구', '010-7894-0564', '010-3246-1127', 91101, 21103),
('박민수', '1992-04-14', '920414-1349802', '인천시 연수구', '010-4891-7794', '010-0849-8751', 91102, 21101),
('정희정', '1988-05-05', '880505-2398411', '서울시 강서구', '010-9489-0789', '010-4894-7894', 91103, 21104),
('최준호', '1975-06-16', '750616-1090033', '서울시 동작구', '010-1234-5678', '010-8765-4321', 91104, 21105),
('윤서아', '1989-07-07', '890707-2349818', '서울시 관악구', '010-5678-1234', '010-4321-8765', 91103, 21106),
('김민재', '1994-08-18', '940818-1438091', '서울시 구로구', '010-1357-2468', '010-9753-8642', 91104, 21104),
('이하늘', '1982-09-29', '820929-1348713', '부산시 남구', '010-2468-1357', '010-8642-9753', 91201, 21201),
('박서준', '1991-10-10', '911010-1049819', '대구시 수성구', '010-3698-1472', '010-7531-8642', 91202, 21202),
('정나나', '1983-11-21', '831121-2341143', '대구시 중구', '010-1472-3698', '010-6428-7531', 91201, 21203),
('최빛나', '1995-12-12', '951212-2339481', '서울시 성북구', '010-2584-3691', '010-8426-7531', 91202, 21201),
('윤지호', '1984-01-13', '840113-2400193', '경주시 황남동', '010-3691-2584', '010-7531-8426', 91301, 21301),
('김하늘', '1993-02-24', '930224-2348734', '여수시 돌산읍', '010-4713-2586', '010-6317-8426', 91302, 21302),
('이서진', '1980-03-15', '800315-2488394', '서울시 금천구', '010-5824-3697', '010-4205-7531', 91301, 21303),
('박준혁', '1992-04-26', '920426-1349810', '서울시 영등포구', '010-6935-4718', '010-2042-7531', 91302, 21301),
('정민준', '2015-05-17', '150517-3348193', '인천시 남동구', '010-8046-5829', '010-9750-8642', 91401, 21401),
('최윤아', '2016-06-28', '160628-4334187', '서울시 관악구', '010-9157-6930', '010-8640-9753', 91402, 21402),
('윤가은', '2011-07-19', '110719-4247588', '서울시 서대문구', '010-0268-8041', '010-7530-8642', 91401, 21403),
('김지아', '2013-08-30', '130830-4001193', '서울시 마포구', '010-1379-9152', '010-6420-7531', 91402, 21401),
('이도윤', '1986-09-11', '860911-1349813', '울산시 남구', '010-2480-0263', '010-5310-8426', 91101, 21101),
('박지민', '1994-10-22', '941022-2348149', '서울시 성동구', '010-3591-1374', '010-4201-7531', 91102, 21102),
('정수빈', '1987-11-03', '871103-2338183', '서울시 광진구', '010-4702-2485', '010-3092-8642', 91103, 21105),
('최영호', '1993-12-14', '931214-1409138', '수원시 팔달구', '010-5813-3596', '010-1983-9753', 91104, 21104),
('윤다혜', '1982-01-25', '820125-2348193', '서울시 종로구', '010-6924-4707', '010-0874-7531', 91201, 21203);


INSERT INTO DB2024_Disease (DiseaseID, DiseaseName)
VALUES
(01, '고혈압'),
(02, '당뇨병'),
(03, '천식'),
(04, '관절염'),
(05, '알츠하이머병'),
(06, '우울증'),
(07, '불안 장애'),
(08, '편두통'),
(09, '비만'),
(10, '고지혈증'),
(11, '간경변'),
(12, '신부전'),
(13, '폐렴'),
(14, '위염'),
(15, '골다공증'),
(16, '심근경색'),
(17, '만성폐쇄성폐질환'),
(18, '뇌졸중'),
(19, '패혈증'),
(20, '췌장염');

INSERT INTO DB2024_Medication (MedicationID, MedicationName)
VALUES
(01, '아모디핀'),
(02, '메트포르민'),
(03, '살부타몰'),
(04, '이부프로펜'),
(05, '도네페질'),
(06, '플루옥세틴'),
(07, '디아제팜'),
(08, '수마트립탄'),
(09, '오르리스타트'),
(10, '아토르바스타틴'),
(11, '우르소데옥시콜산'),
(12, '푸로세미드'),
(13, '아목시실린'),
(14, '오메프라졸'),
(15, '알렌드로네이트'),
(16, '아스피린'),
(17, '살메테롤'),
(18, '클로피도그렐'),
(19, '반코마이신'),
(20, '판크레리파아제');

INSERT INTO DB2024_Treatment (PatientID, DiseaseID, MedicationID, RecommendedTreatment, Date, Dosage, KTAS)
VALUES
(01, 01, 01, '고혈압 관리: 저염식 식이요법과 규칙적인 운동 병행', '2024-01-01', '10mg', 3),  -- 내과
(02, 02, 02, '당뇨병 관리: 혈당 수치 모니터링 및 저탄수화물 식단', '2024-01-02', '500mg', 4), -- 내과
(03, 09, 09, '비만 관리: 체중 감량을 위한 식이조절 및 운동 프로그램', '2024-01-03', '120mg', 4), -- 내과
(04, 10, 10, '고지혈증 관리: 저지방 식이요법과 정기적인 혈액 검사', '2024-01-04', '10mg', 3), -- 내과
(05, 01, 01, '고혈압 관리: 저염식 식이요법과 규칙적인 운동 병행', '2024-01-01', '10mg', 3), -- 내과
(06, 02, 02, '당뇨병 관리: 혈당 수치 모니터링 및 저탄수화물 식단', '2024-01-02', '500mg', 4), -- 내과
(07, 09, 09, '비만 관리: 체중 감량을 위한 식이조절 및 운동 프로그램', '2024-01-03', '120mg', 4), -- 내과
(08, 10, 10, '고지혈증 관리: 저지방 식이요법과 정기적인 혈액 검사', '2024-01-04', '10mg', 3), -- 내과
(09, 05, 05, '알츠하이머병 관리: 약물치료와 인지기능 유지 훈련', '2024-01-05', '10mg', 5), -- 신경과
(10, 08, 08, '편두통 관리: 스트레스 관리와 적절한 수면 확보', '2024-01-06', '50mg', 3), -- 신경과
(11, 18, 18, '뇌졸중 관리: 혈전 용해제와 재활 치료 병행', '2024-01-07', '75mg', 5), -- 신경과
(12, 05, 05, '알츠하이머병 관리: 약물치료와 인지기능 유지 훈련', '2024-01-23', '10mg', 5), -- 신경과
(13, 15, 15, '골다공증 관리: 칼슘 및 비타민 D 보충제와 규칙적인 운동', '2024-01-08', '70mg', 4), -- 정형외과
(14, 04, 04, '관절염 관리: 항염증제 복용과 물리치료 병행', '2024-01-09', '400mg', 3), -- 정형외과
(15, 15, 15, '골다공증 관리: 칼슘 및 비타민 D 보충제와 규칙적인 운동', '2024-01-16', '70mg', 4), -- 정형외과
(16, 15, 15, '골다공증 관리: 칼슘 및 비타민 D 보충제와 규칙적인 운동', '2024-01-16', '70mg', 4), -- 정형외과
(17, 04, 04, '관절염 관리: 항염증제 복용과 물리치료 병행', '2024-01-09', '400mg', 3), -- 소아과
(18, 09, 09, '비만 관리: 체중 감량을 위한 식이조절 및 운동 프로그램', '2024-01-03', '120mg', 4), -- 소아과
(19, 19, 19, '패혈증 관리: 항생제 투여 및 정기적인 모니터링', '2024-01-19', '1g', 4), -- 소아과
(20, 11, 11, '간경변 관리: 알코올 섭취 제한과 간 보호제 복용', '2024-01-20', '250mg', 4), -- 소아과
(21, 02, 02, '당뇨병 관리: 혈당 수치 모니터링 및 저탄수화물 식단', '2024-01-21', '500mg', 4), -- 내과
(22, 10, 10, '고지혈증 관리: 저지방 식이요법과 정기적인 혈액 검사', '2024-01-22', '10mg', 3), -- 내과
(23, 01, 01, '고혈압 관리: 저염식 식이요법과 규칙적인 운동 병행', '2024-01-01', '10mg', 3), -- 내과
(24, 02, 02, '당뇨병 관리: 혈당 수치 모니터링 및 저탄수화물 식단', '2024-01-02', '500mg', 4), -- 내과
(25, 18, 18, '뇌졸중 관리: 혈전 용해제와 재활 치료 병행', '2024-01-25', '75mg', 5); -- 신경과

CREATE INDEX idx_doctor_department ON DB2024_Doctor (DoctorDepartment);
CREATE INDEX idx_nurse_department ON DB2024_Nurse (NurseDepartment);
CREATE INDEX idx_patient_doctor ON DB2024_patient (DoctorID);
CREATE INDEX idx_patient_nurse ON DB2024_patient (NurseID);

CREATE VIEW PatientTreatmentView AS
SELECT
    p.PatientID,
    p.Name AS PatientName,  -- 변경된 부분: 필드 이름의 적절한 참조
    p.Birth AS DateOfBirth,
    p.ResidentNum,  -- 수정된 부분: 콤마 추가
    p.Address,
    p.Phone AS PatientPhone,
    p.GuardianPhone,
    t.DiseaseID,
    d.DiseaseName,
    t.MedicationID,
    m.MedicationName,
    t.RecommendedTreatment,
    t.Date AS TreatmentDate,
    t.Dosage,
    t.KTAS
FROM
    DB2024_patient p
JOIN
    DB2024_Treatment t ON p.PatientID = t.PatientID
JOIN
    DB2024_Disease d ON t.DiseaseID = d.DiseaseID
JOIN
    DB2024_Medication m ON t.MedicationID = m.MedicationID;
    
    -- 환자 테이블의 환자 이름, 약물, 의사 테이블과 치료 테이블을 결합한 뷰
CREATE VIEW PatientDoctorTreatmentView AS
SELECT
    p.PatientID,
    p.Name AS PatientName,  -- 변경된 부분: 필드 이름의 적절한 참조
    p.Birth AS DateOfBirth,
    p.ResidentNum,  -- 수정된 부분: 콤마 추가
    p.Address,
    p.Phone AS PatientPhone,
    p.GuardianPhone,
    dct.DoctorID,
    dct.DoctorName,
    dct.DoctorDepartment,
    dct.ContactNumber AS DoctorPhone,
    ds.DiseaseID,
    ds.DiseaseName,
    m.MedicationID,
    m.MedicationName,
    t.RecommendedTreatment,
    t.Date AS TreatmentDate,
    t.Dosage,
    t.KTAS
FROM
    DB2024_patient p
JOIN
    DB2024_Treatment t ON p.PatientID = t.PatientID
JOIN
    DB2024_Doctor dct ON p.DoctorID = dct.DoctorID
JOIN
    DB2024_Disease ds ON t.DiseaseID = ds.DiseaseID
JOIN
    DB2024_Medication m ON t.MedicationID = m.MedicationID;
    
CREATE VIEW DB2024_LoginDB AS
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
    
    -- 환자의 개인정보는 숨기는 view
CREATE VIEW DB2024_patient_privacy AS
SELECT
PatientID,
Name, 
Birth, 
Address, 
Phone, 
GuardianPhone,  
NurseID,
DoctorID
FROM
    DB2024_patient;

-- 출근해 있는 인원만 보는 view

CREATE VIEW CurrentOnDutyStaff AS
SELECT DoctorID AS ID, DoctorName AS Name, DoctorDepartment AS Department, ContactNumber, 'Doctor' AS Role
FROM DB2024_Doctor
WHERE CURTIME() BETWEEN StartTime AND EndTime
UNION
SELECT NurseID AS ID, NurseName AS Name, NurseDepartment AS Department, ContactNumber, 'Nurse' AS Role
FROM DB2024_Nurse
WHERE CURTIME() BETWEEN StartTime AND EndTime;

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

    
-- DROP DATABASE DB2024Team02;