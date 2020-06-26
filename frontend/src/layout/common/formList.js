import React from 'react';
import { Form, Input, Button} from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';

const defaultFormItemLayout = {
    labelCol: {
      xs: { span: 24 },
      sm: { span: 4 },
    },
    wrapperCol: {
      xs: { span: 24 },
      sm: { span: 10 },
    },
  };

const calcFormItemLayoutWithOutLabel = (formItemLayout) => {

  const sizeList = ['xs', 'sm'];
  const {labelCol, wrapperCol} = formItemLayout;

  var sizeMap = new Object();
  for(var i in sizeList) {
    const size = sizeList[i];
    sizeMap[size] = {'span': wrapperCol[size].span, 'offset': labelCol[size].span}
  }
  console.log(formItemLayout);
  console.log(sizeMap);
  return {
    wrapperCol: sizeMap
  }

}


  // 目前只能渲染一个字段
export const FormList = ({
    name,                           //对应list对象的名字
    label='NoName', 
    fieldName,                 //要渲染对象具体的字段
    rules, 
    formItemLayout=defaultFormItemLayout, 
    formItemLayoutWithOutLabel}) => {
    
    formItemLayoutWithOutLabel = calcFormItemLayoutWithOutLabel(formItemLayout);

    return (
        <Form.List name={name}>
        {(fields, { add, remove }) => {
          fields = fields == null ? new Array() : fields;
          return (
            <div>
              {fields.map((field, index) => (
                <Form.Item
                  {...(index === 0 ? formItemLayout : formItemLayoutWithOutLabel)}
                  label={index === 0 ? label : ''}
                  required={false}
                  key={field.key}
                >
              {console.log(field)}
                  <Form.Item
                    {...field}
                    name={[field.name, fieldName]}
                    validateTrigger={['onChange', 'onBlur']}
                    rules={rules}
                    noStyle
                  >
                    <Input style={{ width: '80%', marginRight: 8 }} />
                  </Form.Item>
                  {fields.length > 0 ? (
                    <MinusCircleOutlined
                      className="dynamic-delete-button"
                      onClick={() => {
                        remove(field.name);
                      }}
                    />
                  ) : null}
                </Form.Item>
              ))}
              <Form.Item {...formItemLayoutWithOutLabel}>
                <Button
                  type="dashed"
                  onClick={() => {
                    add();
                  }}
                  style={{ width: '60%' }}
                >
                  <PlusOutlined /> 新增
                </Button>
              </Form.Item>
            </div>
          );
        }}
      </Form.List>
    )
}