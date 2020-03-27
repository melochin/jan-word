import React, {useEffect} from 'react';
import { Row,  } from 'antd';
import { Calendar } from '@antv/g2plot';
import {list} from '../../action/memoryAction';

// const list = async ( ) => {
//     return {
//         start: '2018-10-31',
//         end: '2019-10-31',
//         data: [

//             { "date": "2019-10-31", "count": 5,}
//         ]
//     }
// };

const MemoryCalendar = () => {

    const id = 'calender';

    const getCalendar = async () => {

        const res = await list();

        const sum = res.data.reduce((total, current) => total + current.count, 0);

        //TODO 一次打卡看不见
        return new Calendar(document.getElementById(id), {
            description: {
                visible: true,
                text: `一年中共打卡${sum}次`,
              },
              data: res.data,
              dateField: 'date',
              valueField: 'count',
              dateRange: [res.start, res.end],
              padding: 'auto',
              colors: '#f0f2f5-#418318-#418318',
              label: {
                visible: true,
              },
        })
    }

    useEffect(() => {
        const func = async  () => {
            const calender = await getCalendar();
            calender.render();
        };
        func();
    },[])

    return (
            <Row id={id} style={{height: "12rem"}}>
            </Row>
    )

}

export default MemoryCalendar;