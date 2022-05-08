import { Paper, TableContainer } from "@mui/material";
import React from "react";
import { DashboardLayout } from "../templates/dashboard/DashboardLayout";
import Orders from "../templates/dashboard/Orders";


const ProductPage: React.FC = () => {
  return (
    <DashboardLayout title="商品ページ">
      <Paper sx={{ p: 2 }}>
        <Orders></Orders>
      </Paper>
    </DashboardLayout>

  );
};

export default ProductPage;
