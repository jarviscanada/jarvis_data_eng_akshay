#Question 1

select 
	cpu_number, 
	host_id, 
	total_mem 
from 
	host_info 
order by 
	cpu_number, 
	total_mem desc;

#Question 2
select 
  host_info.id as host_id, 
  host_info.hostname as host_name, 
  rounded_usage.rounded_timestamp as timestamp, 
  round(
    avg(
      host_info.total_mem / 1000 - rounded_usage.memory_free
    ) / (host_info.total_mem / 1000) * 100, 
    2
  ) as avg_used_mem_percentage 
from 
  (
    select 
      *, 
      date_trunc('hour', timestamp) + date_part('minute', timestamp):: int / 5 * interval '5 min' as rounded_timestamp 
    from 
      host_usage
  ) as rounded_usage 
  inner join host_info on rounded_usage.host_id = host_info.id 
group by 
  host_info.id, 
  rounded_usage.rounded_timestamp 
order by 
  host_info.id, 
  rounded_usage.rounded_timestamp;
