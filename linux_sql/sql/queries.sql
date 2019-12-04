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
	host_usage.id,
	 host_info.hostname, 
	host_info.mem_total,
	(
		avg(
			(host_info.mem_total - host_usage.memory_free)/host_info.mem_total
		) * 100
	)::int as avg_mem_used
from 
	host_usage
	inner join host_info on host_usage.id = host_info.id
group by 
	host_usage.id, 
	host_info.hostname, 
	host_info.mem_total,
	date_trunc('hour', host_usage.curr_time) + interval '5 minute' * round(
		date_part('minute', host_usage.curr_time) / 5.0
	)
order by host_usage.id;
