fare calculate rule consists of two parts: 
    1.general peak hour config 
    2.rule for specific route
these two parts are assembled together to class Rule,
rule config size is expected to be small, so it always can fit into memory
note that if rule can be updated, it's better to be tagged with version and start-to-effect-time, which means every version of rule is immutable,
immutable data can be cached safely, and in distributed system, new config version can take effect at the same time based on timestamp

history fare charging has influence on calculation, to maintain history record, we store charge detail and weekly statistic.
weekly statistic will have 7 columns, representing each day of week
we use epoch timestamp of first day of week at 00:00:00 to represent this week, note that first day of week may not be proper for business use, which needs more discussing

as fare rule become more complex, RuleSelector may need more parameter to choose matching rule