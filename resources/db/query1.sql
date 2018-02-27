SELECT ip FROM access_history 
WHERE date BETWEEN <start_date> AND <end_date>
GROUP BY IP
HAVING COUNT(ip) > <threshold>