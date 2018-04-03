import React from 'react';
import { Tabs } from 'antd';
import SlideMenu from '../common/SlideMenu';
import TopNavi from '../common/TopNavi';

export default class IndexMain extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
        };
    }

    onChange(activeKey) {
        this.setState({ activeKey });
    }


    render() {
        let clientHeight = document.body.clientHeight;
        let contentHeight = clientHeight - 50 - 40 - 24;
        return (
            <div className="ant-layout-aside">
                <SlideMenu />
                <div className="ant-layout-main">
                    <div className="ant-layout-header">
                        <TopNavi/>
                    </div>
                    <div className="ant-layout-container">
                        <div className="ant-layout-content" style={{minHeight: contentHeight + "px"}}>
                                {this.props.children}
                        </div>
                    </div>
                    <div className="ant-layout-footer">
                        世仓智能仓储设备(上海)股份有限公司
                    </div>
                </div>
            </div>
        );
    }
}