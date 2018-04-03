
delimiter $$
create function _nextval(n varchar(50)) returns integer   
begin  
declare _cur int;
declare _maxValue int;
set _cur=(select current_value from tb_sequence where name= n); 
 if n = 'seq_mckey' then
 set _maxValue = 9998;
elseif n = 'seq_xml' then
 set _maxValue = 99998;
  elseif n = 'seq_dock' then
 set _maxValue = 9998;
else 
 set _maxValue = 99999998;
end if;
 if _cur < _maxValue then
	update tb_sequence  
	  set current_value = _cur + _increment  
	 where name=n;  
 else
	update tb_sequence set current_value = 1 where name = n;
 end if;
return _cur;
end
$$
delimiter ; 