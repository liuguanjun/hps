SELECT a.areaName      ,h.isBusiness      ,p.electricityPrice      ,b.storiedBuildingID      ,MAX(p.createDateTime)  FROM [ZSYJ].[dbo].[storiedBuilding] b, [ZSYJ].[dbo].area a, [ZSYJ].[dbo].house h  , [ZSYJ].[dbo].[electricityPayment] p  where b.areaID = a.areaID  and b.storiedBuildingID = h.storiedBuildingID  and h.houseID = p.houseID  and p.electricityPrice != 1.040  and (h.isBusiness = 0 or p.electricityPrice != 0.525)  group by areaName, isBusiness, electricityPrice, b.storiedBuildingID  order by storiedBuildingID, areaName, isBusiness, electricityPrice  /****** Script for SelectTopNRows command from SSMS  ******/SELECT TOP 1000 [houseID]      ,h.[storiedBuildingID]      ,b.localNum      ,a.areaName      ,[localUnitNum]      ,[floorNum]      ,[localDoorNum]      ,[orderNum]      ,[heatingArea]      ,[enjoyPreferentialArea]      ,[maintainArea]      ,[InstrumentationNumOEM]      ,[InstrumentationNumCUM]      ,[hasHeating]      ,[hasHouseMaintain]      ,[hasElectric]      ,[hasElectricMaintain]      ,[hasCorridorLight]      ,[hasClean]      ,[isBusiness]      ,[createDate]  FROM [ZSYJ].[dbo].[house] h , [ZSYJ].[dbo].storiedBuilding b, [ZSYJ].[dbo].area a   where houseID = 91 and h.storiedBuildingID = b.storiedBuildingID   and b.areaID = a.areaID   order by houseID   SELECT     h.id, r.charge_date, if (r.current_surplus is null, 0, r.current_surplus),     sum(if (c.charge_record_id is null, c.electricCount, 0))FROM    hps.hps_house h        left outer join    hps.hps_electric_charge_record r ON h.id = r.house_id        left outer join    hps.hps_electric_chaobiao c ON h.id = c.house_idwhere    (r.charge_date = (select         max(r2.charge_date)    from        hps.hps_electric_charge_record r2    where        r2.house_id = h.id) or r.charge_date is null)group by h.id