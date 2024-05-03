import {
  Form,
  Grid,
  Label,
  Radio,
  Select,
  Fieldset,
  TextInput,
  GridContainer,
  StepIndicator,
  StepIndicatorStep,
  ButtonGroup,
  Button,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";
import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";

export default function FilingInformation() {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleFilingInfo = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    // @ts-expect-error untyped form elements but we need the values
    const isDependent = e.currentTarget.elements.dependent.value === "Yes";

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      filingStatus: e.currentTarget.elements.filing_status.value,
      // @ts-expect-error untyped form elements but we need the values
      dependents: e.currentTarget.elements.dependents.value,
      isDependent,
    };

    console.log(formData);
    return;

    navigate("/filing/w2");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label={t("personal.title")} status="complete" />
          <StepIndicatorStep label={t("filing-info.title")} status="current" />
          <StepIndicatorStep label={t("w2.title")} />
          <StepIndicatorStep label="Self Employment" />
          <StepIndicatorStep label="Credits & Deductions" />
          <StepIndicatorStep label="Review" />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handleFilingInfo} className="w-full">
          <Fieldset legend={t("filing-info.desc")}>
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="filing_status" requiredMarker>
                  {t("filing-info.status")}
                </Label>
                <Select id="filing_status" name="filing_status" required>
                  <option>{t("select")}</option>
                  <option value="Single">{t("filing-info.single")}</option>
                  {/* TODO: what is the backend expecting here for value? */}
                  <option value="Married">{t("filing-info.married")}</option>
                  <option value="Head of Household">
                    {t("filing-info.hoh")}
                  </option>
                </Select>
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="dependents" requiredMarker>
                  {t("filing-info.dependents")}
                </Label>
                <TextInput
                  id="dependents"
                  name="dependents"
                  type="number"
                  defaultValue={0}
                  required
                />
              </Grid>
            </Grid>

            <Grid row>
              <Grid tablet={{ col: true }}>
                <p>{t("filing-info.isDependent")}</p>
                <Radio
                  id="dependent-yes"
                  name="dependent"
                  value="Yes"
                  label={t("y")}
                />
                <Radio
                  id="dependent-no"
                  name="dependent"
                  value="No"
                  label={t("n")}
                />
              </Grid>
            </Grid>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/personal"
                  className="usa-button usa-button--outline"
                >
                  {t("back")}
                </Link>
                <Button type="submit">{t("continue")}</Button>
              </ButtonGroup>
            </div>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
